package br.org.oficinadasmeninas.domain.transparency;

import java.util.Date;
import java.util.UUID;

public class Document {
    private UUID id;
    private Date effectiveDate;
    private Category category;
    private String previewLink;
    private String downloadLink;
}
