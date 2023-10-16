package com.aem.community.core.components.models;

import org.osgi.annotation.versioning.ConsumerType;

@ConsumerType
public interface BookImage {
    String IMAGE_TITLE = "dc:title";

    String getSrc();

    String getAlt();
}
