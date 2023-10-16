package com.aem.community.core.components.models;

import org.osgi.annotation.versioning.ConsumerType;

import java.util.List;

@ConsumerType
public interface BookList {
    List<BookItem> bookItems();
}
