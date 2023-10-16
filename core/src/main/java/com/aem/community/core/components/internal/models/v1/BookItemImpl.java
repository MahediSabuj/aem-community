package com.aem.community.core.components.internal.models.v1;

import com.adobe.cq.export.json.ExporterConstants;
import com.aem.community.core.components.models.BookImage;
import com.aem.community.core.components.models.BookItem;
import com.drew.lang.annotations.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.factory.ModelFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

@Model(
  adaptables = SlingHttpServletRequest.class,
  adapters = { BookItem.class },
  resourceType = BookItemImpl.RESOURCE_TYPE,
  defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(
  name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
  extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class BookItemImpl implements BookItem {
    public static final String RESOURCE_TYPE = "aem-community/components/book-item";

    private BookImage bookImage;

    @Self
    private SlingHttpServletRequest request;

    @ScriptVariable
    private Resource resource;

    @Inject
    private ModelFactory modelFactory;

    @ValueMapValue
    @Nullable
    @Default(values = "")
    @Named("title")
    String bookTitle;

    @PostConstruct
    protected void init() {
        bookImage = modelFactory.getModelFromWrappedRequest(request, resource, BookImage.class);
    }

    @Override
    public String getSrc() {
        return bookImage == null ? StringUtils.EMPTY : bookImage.getSrc();
    }

    @Override
    public String getAlt() {
        return bookImage == null ? StringUtils.EMPTY : bookImage.getAlt();
    }

    @Override
    public String getBookTitle() {
        return bookTitle;
    }
}
