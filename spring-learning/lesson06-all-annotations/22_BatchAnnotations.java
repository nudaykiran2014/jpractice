package spring_learning.lesson06_all_annotations;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  PART 22: SPRING BATCH ANNOTATIONS                                           ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * Spring Batch is for processing large volumes of data (ETL jobs, reports, etc.)
 * 
 * Requires: spring-boot-starter-batch
 */
public class _22_BatchAnnotations {
    public static void main(String[] args) {
        System.out.println("=== SPRING BATCH ANNOTATIONS ===\n");
        System.out.println("@EnableBatchProcessing → Enable batch processing");
        System.out.println("@StepScope      → Bean scoped to step execution");
        System.out.println("@JobScope       → Bean scoped to job execution");
        System.out.println("@BeforeStep     → Execute before step");
        System.out.println("@AfterStep      → Execute after step");
        System.out.println("@BeforeJob      → Execute before job");
        System.out.println("@AfterJob       → Execute after job");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * BATCH CONFIGURATION
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     @Configuration
 *     @EnableBatchProcessing
 *     public class BatchConfig {
 *         
 *         @Autowired
 *         private JobBuilderFactory jobBuilderFactory;
 *         
 *         @Autowired
 *         private StepBuilderFactory stepBuilderFactory;
 *         
 *         // Define a Job
 *         @Bean
 *         public Job importUserJob(Step step1, Step step2) {
 *             return jobBuilderFactory.get("importUserJob")
 *                 .incrementer(new RunIdIncrementer())
 *                 .start(step1)
 *                 .next(step2)
 *                 .build();
 *         }
 *         
 *         // Define a Step (chunk-based processing)
 *         @Bean
 *         public Step step1(ItemReader<User> reader,
 *                          ItemProcessor<User, User> processor,
 *                          ItemWriter<User> writer) {
 *             return stepBuilderFactory.get("step1")
 *                 .<User, User>chunk(100)  // Process 100 items at a time
 *                 .reader(reader)
 *                 .processor(processor)
 *                 .writer(writer)
 *                 .build();
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @StepScope and @JobScope
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Create beans that are scoped to step/job execution
 *          Allows late binding of job parameters
 * 
 * EXAMPLE:
 * ---------
 *     @Configuration
 *     public class BatchConfig {
 *         
 *         // New instance for each step execution
 *         @Bean
 *         @StepScope
 *         public FlatFileItemReader<User> reader(
 *             @Value("#{jobParameters['inputFile']}") String inputFile
 *         ) {
 *             return new FlatFileItemReaderBuilder<User>()
 *                 .name("userReader")
 *                 .resource(new FileSystemResource(inputFile))
 *                 .delimited()
 *                 .names("id", "name", "email")
 *                 .targetType(User.class)
 *                 .build();
 *         }
 *         
 *         // New instance for each job execution
 *         @Bean
 *         @JobScope
 *         public MyTasklet myTasklet(
 *             @Value("#{jobParameters['date']}") String date
 *         ) {
 *             return new MyTasklet(date);
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * LISTENER ANNOTATIONS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Execute code at various points in job/step lifecycle
 * 
 * EXAMPLE:
 * ---------
 *     @Component
 *     public class JobCompletionListener {
 *         
 *         @BeforeJob
 *         public void beforeJob(JobExecution jobExecution) {
 *             System.out.println("Job starting: " + jobExecution.getJobInstance().getJobName());
 *         }
 *         
 *         @AfterJob
 *         public void afterJob(JobExecution jobExecution) {
 *             if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
 *                 System.out.println("Job completed successfully!");
 *             } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
 *                 System.out.println("Job failed!");
 *             }
 *         }
 *     }
 *     
 *     @Component
 *     public class StepExecutionListener {
 *         
 *         @BeforeStep
 *         public void beforeStep(StepExecution stepExecution) {
 *             System.out.println("Step starting: " + stepExecution.getStepName());
 *         }
 *         
 *         @AfterStep
 *         public ExitStatus afterStep(StepExecution stepExecution) {
 *             System.out.println("Items read: " + stepExecution.getReadCount());
 *             System.out.println("Items written: " + stepExecution.getWriteCount());
 *             return stepExecution.getExitStatus();
 *         }
 *     }
 *     
 *     // Chunk listener
 *     @Component
 *     public class ChunkListener {
 *         
 *         @BeforeChunk
 *         public void beforeChunk(ChunkContext context) {
 *             System.out.println("Chunk starting");
 *         }
 *         
 *         @AfterChunk
 *         public void afterChunk(ChunkContext context) {
 *             System.out.println("Chunk completed");
 *         }
 *         
 *         @AfterChunkError
 *         public void afterChunkError(ChunkContext context) {
 *             System.out.println("Chunk error!");
 *         }
 *     }
 *     
 *     // Item listeners
 *     @Component
 *     public class ItemListeners {
 *         
 *         @BeforeRead
 *         public void beforeRead() { }
 *         
 *         @AfterRead
 *         public void afterRead(User item) { }
 *         
 *         @OnReadError
 *         public void onReadError(Exception ex) { }
 *         
 *         @BeforeProcess
 *         public void beforeProcess(User item) { }
 *         
 *         @AfterProcess
 *         public void afterProcess(User input, User output) { }
 *         
 *         @OnProcessError
 *         public void onProcessError(User item, Exception ex) { }
 *         
 *         @BeforeWrite
 *         public void beforeWrite(List<User> items) { }
 *         
 *         @AfterWrite
 *         public void afterWrite(List<User> items) { }
 *         
 *         @OnWriteError
 *         public void onWriteError(Exception ex, List<User> items) { }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * RUNNING A JOB
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     @RestController
 *     public class JobController {
 *         
 *         @Autowired
 *         private JobLauncher jobLauncher;
 *         
 *         @Autowired
 *         private Job importUserJob;
 *         
 *         @PostMapping("/run-job")
 *         public String runJob(@RequestParam String inputFile) throws Exception {
 *             JobParameters params = new JobParametersBuilder()
 *                 .addString("inputFile", inputFile)
 *                 .addLong("time", System.currentTimeMillis())
 *                 .toJobParameters();
 *             
 *             jobLauncher.run(importUserJob, params);
 *             return "Job started!";
 *         }
 *     }
 * 
 * BATCH CONCEPTS:
 * ----------------
 * | Concept   | Description                              |
 * |-----------|------------------------------------------|
 * | Job       | The entire batch process                 |
 * | Step      | A phase of the job                       |
 * | Chunk     | Items processed together (transaction)   |
 * | Reader    | Reads data from source                   |
 * | Processor | Transforms data                          |
 * | Writer    | Writes data to destination               |
 * | Tasklet   | Simple single-task step                  |
 */
