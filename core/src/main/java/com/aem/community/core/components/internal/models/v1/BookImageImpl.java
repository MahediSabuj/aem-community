package com.aem.community.core.components.internal.models.v1;

import com.adobe.cq.export.json.ExporterConstants;
import com.aem.community.core.components.models.BookImage;
import com.day.cq.dam.api.Asset;
import com.drew.lang.annotations.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.inject.Named;

@Model(
  adaptables = SlingHttpServletRequest.class,
  adapters = { BookImage.class },
  resourceType = BookImageImpl.RESOURCE_TYPE,
  defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(
  name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
  extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class BookImageImpl implements BookImage {
    public static final String RESOURCE_TYPE = "aem-community/components/book-image";

    private String imageSrc;
    private String imageTitle;

    @SlingObject
    private ResourceResolver resourceResolver;

    @ValueMapValue
    @Nullable
    @Default(values = "")
    @Named("fileReference")
    String fileReference;

    @PostConstruct
    protected void init() {
        if(StringUtils.isNotBlank(fileReference)) {
            Resource assetResource = resourceResolver.getResource(fileReference);
            if(assetResource != null) {
                Asset imageAsset = assetResource.adaptTo(Asset.class);
                if(imageAsset != null) {
                    imageSrc = imageAsset.getPath();
                    imageTitle = imageAsset.getMetadataValueFromJcr(IMAGE_TITLE);
                }
            }
        }
    }

    @Override
    public String getSrc() {
        return imageSrc;
    }

    @Override
    public String getAlt() {
        return imageTitle;
    }
}
