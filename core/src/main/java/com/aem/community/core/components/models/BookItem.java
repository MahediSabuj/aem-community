package com.aem.community.core.components.models;

import org.osgi.annotation.versioning.ConsumerType;

@ConsumerType
public interface BookItem extends BookImage {
    String BOOK_TITLE = "title";

    String getBookTitle();
}
