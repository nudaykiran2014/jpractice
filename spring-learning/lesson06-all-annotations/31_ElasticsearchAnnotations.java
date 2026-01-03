package spring_learning.lesson06_all_annotations;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  PART 31: ELASTICSEARCH ANNOTATIONS                                          ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * Elasticsearch is a distributed search and analytics engine.
 * 
 * Requires: spring-boot-starter-data-elasticsearch
 */
public class _31_ElasticsearchAnnotations {
    public static void main(String[] args) {
        System.out.println("=== ELASTICSEARCH ANNOTATIONS ===\n");
        System.out.println("@Document        → Mark class as ES document");
        System.out.println("@Id              → Document identifier");
        System.out.println("@Field           → Customize field mapping");
        System.out.println("@Setting         → Index settings");
        System.out.println("@Mapping         → Custom mapping");
        System.out.println("@Query           → Custom query");
        System.out.println("@Highlight       → Search highlighting");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Document
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Define an Elasticsearch document (like a table row)
 * 
 * EXAMPLE:
 * ---------
 *     @Document(indexName = "products")
 *     public class Product {
 *         
 *         @Id
 *         private String id;
 *         
 *         @Field(type = FieldType.Text, analyzer = "standard")
 *         private String name;
 *         
 *         @Field(type = FieldType.Text)
 *         private String description;
 *         
 *         @Field(type = FieldType.Double)
 *         private Double price;
 *         
 *         @Field(type = FieldType.Keyword)  // Exact match, no analysis
 *         private String category;
 *         
 *         @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
 *         private LocalDateTime createdAt;
 *         
 *         @Field(type = FieldType.Nested)  // Nested object
 *         private List<Review> reviews;
 *     }
 *     
 *     // With custom settings
 *     @Document(
 *         indexName = "products",
 *         shards = 3,
 *         replicas = 2,
 *         refreshInterval = "1s"
 *     )
 *     public class Product { }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Field Types
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     @Document(indexName = "articles")
 *     public class Article {
 *         
 *         @Id
 *         private String id;
 *         
 *         // Full-text search with analysis
 *         @Field(type = FieldType.Text, analyzer = "english")
 *         private String title;
 *         
 *         // Multi-field: searchable AND sortable
 *         @MultiField(
 *             mainField = @Field(type = FieldType.Text),
 *             otherFields = {
 *                 @InnerField(suffix = "keyword", type = FieldType.Keyword)
 *             }
 *         )
 *         private String author;
 *         // Search on: author, Sort on: author.keyword
 *         
 *         // Keyword = exact match (for filters, aggregations)
 *         @Field(type = FieldType.Keyword)
 *         private String status;
 *         
 *         // Numeric types
 *         @Field(type = FieldType.Integer)
 *         private Integer viewCount;
 *         
 *         @Field(type = FieldType.Double)
 *         private Double rating;
 *         
 *         // Boolean
 *         @Field(type = FieldType.Boolean)
 *         private Boolean published;
 *         
 *         // Date
 *         @Field(type = FieldType.Date)
 *         private Date publishedAt;
 *         
 *         // Geo point
 *         @GeoPointField
 *         private GeoPoint location;
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * ELASTICSEARCH REPOSITORY
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     public interface ProductRepository extends ElasticsearchRepository<Product, String> {
 *         
 *         // Query methods
 *         List<Product> findByName(String name);
 *         List<Product> findByCategory(String category);
 *         List<Product> findByPriceBetween(Double min, Double max);
 *         List<Product> findByNameContaining(String keyword);
 *         
 *         // Custom query
 *         @Query("{\"match\": {\"name\": \"?0\"}}")
 *         List<Product> searchByName(String name);
 *         
 *         @Query("{\"bool\": {\"must\": [{\"match\": {\"name\": \"?0\"}}, {\"range\": {\"price\": {\"lte\": ?1}}}]}}")
 *         List<Product> searchByNameAndMaxPrice(String name, Double maxPrice);
 *         
 *         // With highlighting
 *         @Highlight(fields = {
 *             @HighlightField(name = "name"),
 *             @HighlightField(name = "description")
 *         })
 *         SearchHits<Product> findByNameOrDescription(String name, String description);
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * ElasticsearchOperations (Programmatic)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     @Service
 *     public class ProductSearchService {
 *         
 *         @Autowired
 *         private ElasticsearchOperations elasticsearchOperations;
 *         
 *         public SearchHits<Product> search(String keyword) {
 *             Query query = NativeQuery.builder()
 *                 .withQuery(q -> q
 *                     .multiMatch(m -> m
 *                         .query(keyword)
 *                         .fields("name^2", "description")  // name has 2x boost
 *                     )
 *                 )
 *                 .withSort(Sort.by(Sort.Direction.DESC, "price"))
 *                 .withPageable(PageRequest.of(0, 10))
 *                 .build();
 *             
 *             return elasticsearchOperations.search(query, Product.class);
 *         }
 *         
 *         // Full-text search with filters
 *         public SearchHits<Product> searchWithFilters(String keyword, String category, Double maxPrice) {
 *             BoolQuery.Builder boolQuery = new BoolQuery.Builder();
 *             
 *             // Must match keyword
 *             boolQuery.must(m -> m.match(t -> t.field("name").query(keyword)));
 *             
 *             // Filter by category
 *             if (category != null) {
 *                 boolQuery.filter(f -> f.term(t -> t.field("category").value(category)));
 *             }
 *             
 *             // Filter by price
 *             if (maxPrice != null) {
 *                 boolQuery.filter(f -> f.range(r -> r.field("price").lte(JsonData.of(maxPrice))));
 *             }
 *             
 *             Query query = NativeQuery.builder()
 *                 .withQuery(boolQuery.build()._toQuery())
 *                 .build();
 *             
 *             return elasticsearchOperations.search(query, Product.class);
 *         }
 *         
 *         // Aggregations
 *         public Map<String, Long> getCategoryCounts() {
 *             Query query = NativeQuery.builder()
 *                 .withAggregation("categories", 
 *                     Aggregation.of(a -> a.terms(t -> t.field("category"))))
 *                 .build();
 *             
 *             SearchHits<Product> hits = elasticsearchOperations.search(query, Product.class);
 *             // Process aggregation results...
 *         }
 *     }
 * 
 * FIELD TYPES:
 * -------------
 * | Type     | Use Case                    |
 * |----------|-----------------------------| 
 * | Text     | Full-text search            |
 * | Keyword  | Exact match, filters, sort  |
 * | Integer  | Whole numbers               |
 * | Double   | Decimal numbers             |
 * | Boolean  | true/false                  |
 * | Date     | Dates and times             |
 * | Nested   | Array of objects            |
 * | GeoPoint | Lat/lon coordinates         |
 * 
 * CONFIGURATION:
 * ---------------
 *     # application.properties
 *     spring.elasticsearch.uris=http://localhost:9200
 *     spring.elasticsearch.username=elastic
 *     spring.elasticsearch.password=secret
 */
