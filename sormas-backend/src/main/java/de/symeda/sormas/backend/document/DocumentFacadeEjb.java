/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2020 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package de.symeda.sormas.backend.document;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import de.symeda.sormas.api.document.DocumentDto;
import de.symeda.sormas.api.document.DocumentFacade;
import de.symeda.sormas.api.document.DocumentRelatedEntityType;
import de.symeda.sormas.backend.user.UserFacadeEjb;
import de.symeda.sormas.backend.user.UserService;
import de.symeda.sormas.backend.util.DtoHelper;
import de.symeda.sormas.backend.util.ModelConstants;

@Stateless(name = "DocumentFacade")
public class DocumentFacadeEjb implements DocumentFacade {

	@PersistenceContext(unitName = ModelConstants.PERSISTENCE_UNIT_NAME)
	private EntityManager em;

	@EJB
	private UserService userService;
	@EJB
	private DocumentService documentService;
	@EJB
	private DocumentStorageService documentStorageService;

	@Inject
	private Event<DocumentSaved> documentSavedEvent;

	@Override
	public DocumentDto getDocumentByUuid(String uuid) {
		return toDto(documentService.getByUuid(uuid));
	}

	@Override
	public DocumentDto saveDocument(DocumentDto dto, byte[] content) throws IOException {
		Document existingDocument = dto.getUuid() == null ? null : documentService.getByUuid(dto.getUuid());
		if (existingDocument != null) {
			// TODO: add exception message
			throw new EntityExistsException();
		}

		Document document = fromDto(dto);

		document.setStorageReference(documentStorageService.save(document, content));

		documentService.persist(document);
		documentService.doFlush();

		documentSavedEvent.fire(new DocumentSaved(document));

		return toDto(document);
	}

	@Override
	public void deleteDocument(String uuid) {
		// Only mark as delete here; actual deletion will be done in document storage cleanup via cron job
		documentService.markAsDeleted(documentService.getByUuid(uuid));
	}

	@Override
	public List<DocumentDto> getDocumentsRelatedToEntity(DocumentRelatedEntityType type, String uuid) {
		return documentService.getRelatedToEntity(type, uuid).stream().map(DocumentFacadeEjb::toDto).collect(Collectors.toList());
	}

	@Override
	public String isExistingDocument(DocumentRelatedEntityType type, String uuid, String name) {
		return documentService.isExisting(type, uuid, name);
	}

	@Override
	public byte[] read(String uuid) throws IOException {
		Document document = documentService.getByUuid(uuid);
		return documentStorageService.read(document.getStorageReference());
	}

	@Override
	public void cleanupDeletedDocuments() {
		List<Document> deleted = documentService.getDeletedDocuments();
		for (Document document : deleted) {
			documentStorageService.delete(document.getStorageReference());
			documentService.delete(document);
		}
	}

	public Document fromDto(DocumentDto source) {
		Document target = documentService.getByUuid(source.getUuid());
		if (target == null) {
			target = new Document();
			target.setUuid(source.getUuid());
			if (source.getCreationDate() != null) {
				target.setCreationDate(new Timestamp(source.getCreationDate().getTime()));
			}
		}
		DtoHelper.validateDto(source, target);

		target.setUploadingUser(userService.getByReferenceDto(source.getUploadingUser()));
		target.setName(source.getName());
		target.setContentType(source.getContentType());
		target.setSize(source.getSize());
		target.setRelatedEntityUuid(source.getRelatedEntityUuid());
		target.setRelatedEntityType(source.getRelatedEntityType());

		return target;
	}

	// XXX: pseudonimize uploadingUser? Under which conditions?
	public static DocumentDto toDto(Document source) {
		if (source == null) {
			return null;
		}
		DocumentDto target = new DocumentDto();
		DtoHelper.fillDto(target, source);

		target.setUploadingUser(UserFacadeEjb.toReferenceDto(source.getUploadingUser()));
		target.setName(source.getName());
		target.setContentType(source.getContentType());
		target.setSize(source.getSize());
		target.setRelatedEntityUuid(source.getRelatedEntityUuid());
		target.setRelatedEntityType(source.getRelatedEntityType());

		return target;
	}

	@LocalBean
	@Stateless
	public static class DocumentFacadeEjbLocal extends DocumentFacadeEjb {
	}
}
