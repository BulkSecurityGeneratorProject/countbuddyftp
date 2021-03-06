package com.mastertek.web.rest;

import static com.mastertek.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.mastertek.FtpcountbuddyApp;
import com.mastertek.config.ApplicationProperties;
import com.mastertek.domain.FileCatalog;
import com.mastertek.repository.FileCatalogRepository;
import com.mastertek.service.DatabaseService;
import com.mastertek.service.FileCatalogService;
import com.mastertek.service.NotifyService;
import com.mastertek.web.rest.errors.ExceptionTranslator;
import com.mastertek.web.rest.util.CountBuddyUtil;

/**
 * Test class for the FileCatalogResource REST controller.
 *
 * @see FileCatalogResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FtpcountbuddyApp.class)
public class FileCatalogResourceIntTest {

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    private static final Boolean DEFAULT_PROCESSED = false;
    private static final Boolean UPDATED_PROCESSED = true;

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final Instant DEFAULT_INSERT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_INSERT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_PROCESS_FINISH_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PROCESS_FINISH_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DEVICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_UUID = "AAAAAAAAAA";
    private static final String UPDATED_UUID = "BBBBBBBBBB";

    @Autowired
    private FileCatalogRepository fileCatalogRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFileCatalogMockMvc;

    private FileCatalog fileCatalog;
    
    @Autowired
    ApplicationProperties applicationProperties;
    
    @Autowired
    DatabaseService databaseService;

    @Autowired
    FileCatalogService fileCatalogService;

    @Autowired
    NotifyService notifyService;
    
    @Mock
    DatabaseService databaseService2;
    
    List mockData = new ArrayList();
    List mockData2 = new ArrayList();
        
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        fileCatalogService = new FileCatalogService(fileCatalogRepository, applicationProperties, databaseService2,notifyService) ;
        final FileCatalogResource fileCatalogResource = new FileCatalogResource(fileCatalogRepository,applicationProperties,databaseService,fileCatalogService);
        this.restFileCatalogMockMvc = MockMvcBuilders.standaloneSetup(fileCatalogResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
        
        mockData2.add("733935");
        when(databaseService2.findFilesForDelete()).thenReturn(mockData);
        when(databaseService2.getDeviceListofStoreWhichDeletedActivated()).thenReturn(mockData2);
        
        //fileCatalogRepository.deleteAll();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FileCatalog createEntity(EntityManager em) {
        FileCatalog fileCatalog = new FileCatalog()
            .path(DEFAULT_PATH)
            .processed(DEFAULT_PROCESSED)
            .deleted(DEFAULT_DELETED)
            .insert(DEFAULT_INSERT)
            .processFinishDate(DEFAULT_PROCESS_FINISH_DATE)
            .deviceId(DEFAULT_DEVICE_ID)
            .uuid(DEFAULT_UUID);
        return fileCatalog;
    }

    @Before
    public void initTest() {
        fileCatalog = createEntity(em);
    }

    @Test
    @Transactional
    public void createFileCatalog() throws Exception {
        int databaseSizeBeforeCreate = fileCatalogRepository.findAll().size();

        // Create the FileCatalog
        restFileCatalogMockMvc.perform(post("/api/file-catalogs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fileCatalog)))
            .andExpect(status().isCreated());

        // Validate the FileCatalog in the database
        List<FileCatalog> fileCatalogList = fileCatalogRepository.findAll();
        assertThat(fileCatalogList).hasSize(databaseSizeBeforeCreate + 1);
        FileCatalog testFileCatalog = fileCatalogList.get(fileCatalogList.size() - 1);
        assertThat(testFileCatalog.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testFileCatalog.isProcessed()).isEqualTo(DEFAULT_PROCESSED);
        assertThat(testFileCatalog.isDeleted()).isEqualTo(DEFAULT_DELETED);
        assertThat(testFileCatalog.getInsert()).isEqualTo(DEFAULT_INSERT);
        assertThat(testFileCatalog.getProcessFinishDate()).isEqualTo(DEFAULT_PROCESS_FINISH_DATE);
        assertThat(testFileCatalog.getDeviceId()).isEqualTo(DEFAULT_DEVICE_ID);
        assertThat(testFileCatalog.getUuid()).isEqualTo(DEFAULT_UUID);
    }

    @Test
    @Transactional
    public void createFileCatalogWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fileCatalogRepository.findAll().size();

        // Create the FileCatalog with an existing ID
        fileCatalog.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFileCatalogMockMvc.perform(post("/api/file-catalogs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fileCatalog)))
            .andExpect(status().isBadRequest());

        // Validate the FileCatalog in the database
        List<FileCatalog> fileCatalogList = fileCatalogRepository.findAll();
        assertThat(fileCatalogList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkPathIsRequired() throws Exception {
        int databaseSizeBeforeTest = fileCatalogRepository.findAll().size();
        // set the field null
        fileCatalog.setPath(null);

        // Create the FileCatalog, which fails.

        restFileCatalogMockMvc.perform(post("/api/file-catalogs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fileCatalog)))
            .andExpect(status().isBadRequest());

        List<FileCatalog> fileCatalogList = fileCatalogRepository.findAll();
        assertThat(fileCatalogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFileCatalogs() throws Exception {
        // Initialize the database
        fileCatalogRepository.saveAndFlush(fileCatalog);

        // Get all the fileCatalogList
        restFileCatalogMockMvc.perform(get("/api/file-catalogs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fileCatalog.getId().intValue())))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH.toString())))
            .andExpect(jsonPath("$.[*].processed").value(hasItem(DEFAULT_PROCESSED.booleanValue())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].insert").value(hasItem(DEFAULT_INSERT.toString())))
            .andExpect(jsonPath("$.[*].processFinishDate").value(hasItem(DEFAULT_PROCESS_FINISH_DATE.toString())))
            .andExpect(jsonPath("$.[*].deviceId").value(hasItem(DEFAULT_DEVICE_ID.toString())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())));
    }

    @Test
    @Transactional
    public void getFileCatalog() throws Exception {
        // Initialize the database
        fileCatalogRepository.saveAndFlush(fileCatalog);

        // Get the fileCatalog
        restFileCatalogMockMvc.perform(get("/api/file-catalogs/{id}", fileCatalog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(fileCatalog.getId().intValue()))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH.toString()))
            .andExpect(jsonPath("$.processed").value(DEFAULT_PROCESSED.booleanValue()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()))
            .andExpect(jsonPath("$.insert").value(DEFAULT_INSERT.toString()))
            .andExpect(jsonPath("$.processFinishDate").value(DEFAULT_PROCESS_FINISH_DATE.toString()))
            .andExpect(jsonPath("$.deviceId").value(DEFAULT_DEVICE_ID.toString()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFileCatalog() throws Exception {
        // Get the fileCatalog
        restFileCatalogMockMvc.perform(get("/api/file-catalogs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFileCatalog() throws Exception {
        // Initialize the database
        fileCatalogRepository.saveAndFlush(fileCatalog);
        int databaseSizeBeforeUpdate = fileCatalogRepository.findAll().size();

        // Update the fileCatalog
        FileCatalog updatedFileCatalog = fileCatalogRepository.findOne(fileCatalog.getId());
        // Disconnect from session so that the updates on updatedFileCatalog are not directly saved in db
        em.detach(updatedFileCatalog);
        updatedFileCatalog
            .path(UPDATED_PATH)
            .processed(UPDATED_PROCESSED)
            .deleted(UPDATED_DELETED)
            .insert(UPDATED_INSERT)
            .processFinishDate(UPDATED_PROCESS_FINISH_DATE)
            .deviceId(UPDATED_DEVICE_ID)
            .uuid(UPDATED_UUID);

        restFileCatalogMockMvc.perform(put("/api/file-catalogs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFileCatalog)))
            .andExpect(status().isOk());

        // Validate the FileCatalog in the database
        List<FileCatalog> fileCatalogList = fileCatalogRepository.findAll();
        assertThat(fileCatalogList).hasSize(databaseSizeBeforeUpdate);
        FileCatalog testFileCatalog = fileCatalogList.get(fileCatalogList.size() - 1);
        assertThat(testFileCatalog.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testFileCatalog.isProcessed()).isEqualTo(UPDATED_PROCESSED);
        assertThat(testFileCatalog.isDeleted()).isEqualTo(UPDATED_DELETED);
        assertThat(testFileCatalog.getInsert()).isEqualTo(UPDATED_INSERT);
        assertThat(testFileCatalog.getProcessFinishDate()).isEqualTo(UPDATED_PROCESS_FINISH_DATE);
        assertThat(testFileCatalog.getDeviceId()).isEqualTo(UPDATED_DEVICE_ID);
        assertThat(testFileCatalog.getUuid()).isEqualTo(UPDATED_UUID);
    }

    @Test
    @Transactional
    public void updateNonExistingFileCatalog() throws Exception {
        int databaseSizeBeforeUpdate = fileCatalogRepository.findAll().size();

        // Create the FileCatalog

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFileCatalogMockMvc.perform(put("/api/file-catalogs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fileCatalog)))
            .andExpect(status().isCreated());

        // Validate the FileCatalog in the database
        List<FileCatalog> fileCatalogList = fileCatalogRepository.findAll();
        assertThat(fileCatalogList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFileCatalog() throws Exception {
        // Initialize the database
        fileCatalogRepository.saveAndFlush(fileCatalog);
        int databaseSizeBeforeDelete = fileCatalogRepository.findAll().size();

        // Get the fileCatalog
        restFileCatalogMockMvc.perform(delete("/api/file-catalogs/{id}", fileCatalog.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FileCatalog> fileCatalogList = fileCatalogRepository.findAll();
        assertThat(fileCatalogList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FileCatalog.class);
        FileCatalog fileCatalog1 = new FileCatalog();
        fileCatalog1.setId(1L);
        FileCatalog fileCatalog2 = new FileCatalog();
        fileCatalog2.setId(fileCatalog1.getId());
        assertThat(fileCatalog1).isEqualTo(fileCatalog2);
        fileCatalog2.setId(2L);
        assertThat(fileCatalog1).isNotEqualTo(fileCatalog2);
        fileCatalog1.setId(null);
        assertThat(fileCatalog1).isNotEqualTo(fileCatalog2);
    }
    @Test
    //@Transactional
    public void deleteFiles() throws IOException, InterruptedException {
    	
    	
    	ClassLoader classLoader = getClass().getClassLoader();
    	File file = new File(classLoader.getResource("Face_733935_19121_1557049797506.jpg").getFile());
    	//File tempFile = new File( System.getProperty("user.dir")+"\\Face_733935_42839_1566849042090.jpg");
    	//File tempFile = File.createTempFile("Face_733935_19121_1557049797506", ".png");
    	String tDir = System.getProperty("java.io.tmpdir");
    	File tempFile=new File(tDir+"\\Face_733935_19121_1557049797506.png");
    	if (tempFile.exists()) {
    		tempFile.delete();
    	}
    	
    	tempFile.createNewFile();
    	FileUtils.copyFile(file, tempFile);
    	assertThat(tempFile.exists()).isTrue();
    	
    	FileCatalog fileCatalog = new FileCatalog();
    	fileCatalog.setDeleted(false);
    	fileCatalog.setDeviceId("733935");
    	fileCatalog.setPath(tempFile.getAbsolutePath());
    	fileCatalogRepository.save(fileCatalog);
    	mockData.add(new BigInteger(fileCatalog.getId().toString()));
    	
    	
    	fileCatalogService.deleteFiles();
    	fileCatalog = fileCatalogRepository.findOne(fileCatalog.getId());
    	
    	Thread.currentThread().sleep(1000);
    	//assertThat(fileCatalog.isDeleted()).isTrue();
    	assertThat(tempFile.exists()).isFalse();

    }

    @Test
    //@Transactional
    public void findUnprocessedFilesFromDisk() throws Exception {
    	
    	// send file to ftp directory
    	ClassLoader classLoader = getClass().getClassLoader();
    	FileUtils.cleanDirectory(new File(applicationProperties.getFtpDirectory()));
    	File file = new File(classLoader.getResource("Face_733935_19121_1557049797506.jpg").getFile());
    	CountBuddyUtil.sendFtpFile("localhost", applicationProperties.getFtpPort().intValue(), file,applicationProperties.getFtpDefaultUser(),applicationProperties.getFtpDefaultPassord());
    	
    	//delete record for testing...
    	fileCatalogRepository.deleteAll();
    	List<FileCatalog> list = fileCatalogRepository.findAll();
        assertThat(list.size()).isEqualTo(0);

    	
    	fileCatalogService.findUnprocessedFilesFromDisk();
    	
    	list = fileCatalogRepository.findAll();
    	assertThat(list.size()).isEqualTo(1);

    }
    
}
