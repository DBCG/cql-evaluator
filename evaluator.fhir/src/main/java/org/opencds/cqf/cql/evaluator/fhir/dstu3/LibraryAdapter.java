package org.opencds.cqf.cql.evaluator.fhir.dstu3;

import java.util.List;
import java.util.stream.Collectors;

import org.hl7.fhir.dstu3.model.Library;
import org.hl7.fhir.dstu3.model.Attachment;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.ICompositeType;
import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.context.FhirVersionEnum;

public class LibraryAdapter implements org.opencds.cqf.cql.evaluator.fhir.api.LibraryAdapter {

    private Library library;

    public LibraryAdapter(IBaseResource library) {
        if (library == null) {
            throw new IllegalArgumentException("library can not be null");
        }

        if (!library.fhirType().equals("Library")) {
            throw new IllegalArgumentException("resource passed as library argument is not a Library resource");
        }

        if (!library.getStructureFhirVersionEnum().equals(FhirVersionEnum.DSTU3)) {
            throw new IllegalArgumentException("library is incorrect fhir version for this adapter");
        }

        this.library = (Library) library;
    }

    protected Library getLibrary() {
        return this.library;
    }

    @Override
    public IBaseResource get() {
        return this.library;
    }

    @Override
    public IIdType getId() {
        return this.getLibrary().getIdElement();
    }

    @Override
    public void setId(IIdType id) {
        this.getLibrary().setId(id);
    }

    @Override
    public String getName() {
        return this.getLibrary().getName();
    }

    @Override
    public void setName(String name) {
        this.getLibrary().setName(name);
    }

    @Override
    public String getUrl() {
        return this.getLibrary().getUrl();
    }

    @Override
    public void setUrl(String url) {
        this.getLibrary().setUrl(url);
    }

    @Override
    public String getVersion() {
        return this.getLibrary().getVersion();
    }

    @Override
    public void setVersion(String version) {
        this.getLibrary().setVersion(version);
    }

    @Override
    public Boolean hasContent() {
        return this.getLibrary().hasContent();
    }

    @Override
    public List<ICompositeType> getContent() {
        return this.getLibrary().getContent().stream().collect(Collectors.toList());
    }

    @Override
    public void setContent(List<ICompositeType> attachments) {
        List<Attachment> castAttachments = attachments.stream().map(x -> (Attachment) x).collect(Collectors.toList());
        this.getLibrary().setContent(castAttachments);
    }

    @Override
    public ICompositeType addContent() {
        return this.getLibrary().addContent();
    }
}