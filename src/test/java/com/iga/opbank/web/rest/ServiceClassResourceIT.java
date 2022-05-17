package com.iga.opbank.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.iga.opbank.IntegrationTest;
import com.iga.opbank.domain.ServiceClass;
import com.iga.opbank.repository.ServiceClassRepository;
import com.iga.opbank.service.dto.ServiceClassDTO;
import com.iga.opbank.service.mapper.ServiceClassMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ServiceClassResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ServiceClassResourceIT {

    private static final String DEFAULT_NOM_SERVICE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_SERVICE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/service-classes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ServiceClassRepository serviceClassRepository;

    @Autowired
    private ServiceClassMapper serviceClassMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restServiceClassMockMvc;

    private ServiceClass serviceClass;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceClass createEntity(EntityManager em) {
        ServiceClass serviceClass = new ServiceClass().nomService(DEFAULT_NOM_SERVICE);
        return serviceClass;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceClass createUpdatedEntity(EntityManager em) {
        ServiceClass serviceClass = new ServiceClass().nomService(UPDATED_NOM_SERVICE);
        return serviceClass;
    }

    @BeforeEach
    public void initTest() {
        serviceClass = createEntity(em);
    }

    @Test
    @Transactional
    void createServiceClass() throws Exception {
        int databaseSizeBeforeCreate = serviceClassRepository.findAll().size();
        // Create the ServiceClass
        ServiceClassDTO serviceClassDTO = serviceClassMapper.toDto(serviceClass);
        restServiceClassMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceClassDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ServiceClass in the database
        List<ServiceClass> serviceClassList = serviceClassRepository.findAll();
        assertThat(serviceClassList).hasSize(databaseSizeBeforeCreate + 1);
        ServiceClass testServiceClass = serviceClassList.get(serviceClassList.size() - 1);
        assertThat(testServiceClass.getNomService()).isEqualTo(DEFAULT_NOM_SERVICE);
    }

    @Test
    @Transactional
    void createServiceClassWithExistingId() throws Exception {
        // Create the ServiceClass with an existing ID
        serviceClass.setId(1L);
        ServiceClassDTO serviceClassDTO = serviceClassMapper.toDto(serviceClass);

        int databaseSizeBeforeCreate = serviceClassRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceClassMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceClassDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceClass in the database
        List<ServiceClass> serviceClassList = serviceClassRepository.findAll();
        assertThat(serviceClassList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomServiceIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceClassRepository.findAll().size();
        // set the field null
        serviceClass.setNomService(null);

        // Create the ServiceClass, which fails.
        ServiceClassDTO serviceClassDTO = serviceClassMapper.toDto(serviceClass);

        restServiceClassMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceClassDTO))
            )
            .andExpect(status().isBadRequest());

        List<ServiceClass> serviceClassList = serviceClassRepository.findAll();
        assertThat(serviceClassList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllServiceClasses() throws Exception {
        // Initialize the database
        serviceClassRepository.saveAndFlush(serviceClass);

        // Get all the serviceClassList
        restServiceClassMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceClass.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomService").value(hasItem(DEFAULT_NOM_SERVICE)));
    }

    @Test
    @Transactional
    void getServiceClass() throws Exception {
        // Initialize the database
        serviceClassRepository.saveAndFlush(serviceClass);

        // Get the serviceClass
        restServiceClassMockMvc
            .perform(get(ENTITY_API_URL_ID, serviceClass.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(serviceClass.getId().intValue()))
            .andExpect(jsonPath("$.nomService").value(DEFAULT_NOM_SERVICE));
    }

    @Test
    @Transactional
    void getNonExistingServiceClass() throws Exception {
        // Get the serviceClass
        restServiceClassMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewServiceClass() throws Exception {
        // Initialize the database
        serviceClassRepository.saveAndFlush(serviceClass);

        int databaseSizeBeforeUpdate = serviceClassRepository.findAll().size();

        // Update the serviceClass
        ServiceClass updatedServiceClass = serviceClassRepository.findById(serviceClass.getId()).get();
        // Disconnect from session so that the updates on updatedServiceClass are not directly saved in db
        em.detach(updatedServiceClass);
        updatedServiceClass.nomService(UPDATED_NOM_SERVICE);
        ServiceClassDTO serviceClassDTO = serviceClassMapper.toDto(updatedServiceClass);

        restServiceClassMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceClassDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(serviceClassDTO))
            )
            .andExpect(status().isOk());

        // Validate the ServiceClass in the database
        List<ServiceClass> serviceClassList = serviceClassRepository.findAll();
        assertThat(serviceClassList).hasSize(databaseSizeBeforeUpdate);
        ServiceClass testServiceClass = serviceClassList.get(serviceClassList.size() - 1);
        assertThat(testServiceClass.getNomService()).isEqualTo(UPDATED_NOM_SERVICE);
    }

    @Test
    @Transactional
    void putNonExistingServiceClass() throws Exception {
        int databaseSizeBeforeUpdate = serviceClassRepository.findAll().size();
        serviceClass.setId(count.incrementAndGet());

        // Create the ServiceClass
        ServiceClassDTO serviceClassDTO = serviceClassMapper.toDto(serviceClass);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceClassMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceClassDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(serviceClassDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceClass in the database
        List<ServiceClass> serviceClassList = serviceClassRepository.findAll();
        assertThat(serviceClassList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchServiceClass() throws Exception {
        int databaseSizeBeforeUpdate = serviceClassRepository.findAll().size();
        serviceClass.setId(count.incrementAndGet());

        // Create the ServiceClass
        ServiceClassDTO serviceClassDTO = serviceClassMapper.toDto(serviceClass);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceClassMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(serviceClassDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceClass in the database
        List<ServiceClass> serviceClassList = serviceClassRepository.findAll();
        assertThat(serviceClassList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamServiceClass() throws Exception {
        int databaseSizeBeforeUpdate = serviceClassRepository.findAll().size();
        serviceClass.setId(count.incrementAndGet());

        // Create the ServiceClass
        ServiceClassDTO serviceClassDTO = serviceClassMapper.toDto(serviceClass);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceClassMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceClassDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceClass in the database
        List<ServiceClass> serviceClassList = serviceClassRepository.findAll();
        assertThat(serviceClassList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateServiceClassWithPatch() throws Exception {
        // Initialize the database
        serviceClassRepository.saveAndFlush(serviceClass);

        int databaseSizeBeforeUpdate = serviceClassRepository.findAll().size();

        // Update the serviceClass using partial update
        ServiceClass partialUpdatedServiceClass = new ServiceClass();
        partialUpdatedServiceClass.setId(serviceClass.getId());

        restServiceClassMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceClass.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedServiceClass))
            )
            .andExpect(status().isOk());

        // Validate the ServiceClass in the database
        List<ServiceClass> serviceClassList = serviceClassRepository.findAll();
        assertThat(serviceClassList).hasSize(databaseSizeBeforeUpdate);
        ServiceClass testServiceClass = serviceClassList.get(serviceClassList.size() - 1);
        assertThat(testServiceClass.getNomService()).isEqualTo(DEFAULT_NOM_SERVICE);
    }

    @Test
    @Transactional
    void fullUpdateServiceClassWithPatch() throws Exception {
        // Initialize the database
        serviceClassRepository.saveAndFlush(serviceClass);

        int databaseSizeBeforeUpdate = serviceClassRepository.findAll().size();

        // Update the serviceClass using partial update
        ServiceClass partialUpdatedServiceClass = new ServiceClass();
        partialUpdatedServiceClass.setId(serviceClass.getId());

        partialUpdatedServiceClass.nomService(UPDATED_NOM_SERVICE);

        restServiceClassMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceClass.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedServiceClass))
            )
            .andExpect(status().isOk());

        // Validate the ServiceClass in the database
        List<ServiceClass> serviceClassList = serviceClassRepository.findAll();
        assertThat(serviceClassList).hasSize(databaseSizeBeforeUpdate);
        ServiceClass testServiceClass = serviceClassList.get(serviceClassList.size() - 1);
        assertThat(testServiceClass.getNomService()).isEqualTo(UPDATED_NOM_SERVICE);
    }

    @Test
    @Transactional
    void patchNonExistingServiceClass() throws Exception {
        int databaseSizeBeforeUpdate = serviceClassRepository.findAll().size();
        serviceClass.setId(count.incrementAndGet());

        // Create the ServiceClass
        ServiceClassDTO serviceClassDTO = serviceClassMapper.toDto(serviceClass);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceClassMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, serviceClassDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(serviceClassDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceClass in the database
        List<ServiceClass> serviceClassList = serviceClassRepository.findAll();
        assertThat(serviceClassList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchServiceClass() throws Exception {
        int databaseSizeBeforeUpdate = serviceClassRepository.findAll().size();
        serviceClass.setId(count.incrementAndGet());

        // Create the ServiceClass
        ServiceClassDTO serviceClassDTO = serviceClassMapper.toDto(serviceClass);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceClassMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(serviceClassDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceClass in the database
        List<ServiceClass> serviceClassList = serviceClassRepository.findAll();
        assertThat(serviceClassList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamServiceClass() throws Exception {
        int databaseSizeBeforeUpdate = serviceClassRepository.findAll().size();
        serviceClass.setId(count.incrementAndGet());

        // Create the ServiceClass
        ServiceClassDTO serviceClassDTO = serviceClassMapper.toDto(serviceClass);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceClassMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(serviceClassDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceClass in the database
        List<ServiceClass> serviceClassList = serviceClassRepository.findAll();
        assertThat(serviceClassList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteServiceClass() throws Exception {
        // Initialize the database
        serviceClassRepository.saveAndFlush(serviceClass);

        int databaseSizeBeforeDelete = serviceClassRepository.findAll().size();

        // Delete the serviceClass
        restServiceClassMockMvc
            .perform(delete(ENTITY_API_URL_ID, serviceClass.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ServiceClass> serviceClassList = serviceClassRepository.findAll();
        assertThat(serviceClassList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
