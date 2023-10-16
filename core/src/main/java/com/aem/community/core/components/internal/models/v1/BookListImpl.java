package com.aem.community.core.components.internal.models.v1;

import com.adobe.cq.export.json.ExporterConstants;
import com.aem.community.core.components.models.BookItem;
import com.aem.community.core.components.models.BookList;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.drew.lang.annotations.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.factory.ModelFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jcr.Session;
import java.util.*;

@Model(
  adaptables = SlingHttpServletRequest.class,
  adapters = { BookList.class },
  resourceType = BookListImpl.RESOURCE_TYPE,
  defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(
  name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
  extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class BookListImpl implements BookList {
    public static final String RESOURCE_TYPE = "aem-community/components/book-list";

    private List<BookItem> bookItems;

    @Self
    private SlingHttpServletRequest request;

    @SlingObject
    private ResourceResolver resourceResolver;

    @Inject
    private ModelFactory modelFactory;

    @ValueMapValue
    @Nullable
    @Default(values = "")
    @Named("bookListRoot")
    String rootPath;

    @PostConstruct
    protected void init() {
        bookItems = new ArrayList<>();
        if(StringUtils.isNotBlank(rootPath)) {
            SearchResult result = searchBookItems(rootPath);

            if (result != null && result.getResources() != null) {
                Iterator<Resource> resultIterator = result.getResources();

                while (resultIterator.hasNext()) {
                    Resource resource = resultIterator.next();
                    BookItem bookImage = modelFactory.getModelFromWrappedRequest(request, resource, BookItem.class);
                    if(bookImage != null) {
                        bookItems.add(bookImage);
                    }
                }
            }
        }
    }

    public SearchResult searchBookItems(String rootPath) {
        Map<String, String> predicateMap = new HashMap<>();
        predicateMap.put("path", rootPath);
        predicateMap.put("property", "sling:resourceType");
        predicateMap.put("property.value", "aem-community/components/book-item");

        Session session = resourceResolver.adaptTo(Session.class);
        QueryBuilder queryBuilder = resourceResolver.adaptTo(QueryBuilder.class);
        Query query = queryBuilder.createQuery(PredicateGroup.create(predicateMap), session);

        return query.getResult();
    }


    @Override
    public List<BookItem> bookItems() {
        return bookItems;
    }
}
