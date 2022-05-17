package com.iga.opbank.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.iga.opbank.IntegrationTest;
import com.iga.opbank.domain.PaimentFacture;
import com.iga.opbank.repository.PaimentFactureRepository;
import com.iga.opbank.service.PaimentFactureService;
import com.iga.opbank.service.dto.PaimentFactureDTO;
import com.iga.opbank.service.mapper.PaimentFactureMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PaimentFactureResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PaimentFactureResourceIT {

    private static final Long DEFAULT_REFERANCE = 1L;
    private static final Long UPDATED_REFERANCE = 2L;

    private static final String ENTITY_API_URL = "/api/paiment-factures";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PaimentFactureRepository paimentFactureRepository;

    @Mock
    private PaimentFactureRepository paimentFactureRepositoryMock;

    @Autowired
    private PaimentFactureMapper paimentFactureMapper;

    @Mock
    private PaimentFactureService paimentFactureServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaimentFactureMockMvc;

    private PaimentFacture paimentFacture;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaimentFacture createEntity(EntityManager em) {
        PaimentFacture paimentFacture = new PaimentFacture().referance(DEFAULT_REFERANCE);
        return paimentFacture;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaimentFacture createUpdatedEntity(EntityManager em) {
        PaimentFacture paimentFacture = new PaimentFacture().referance(UPDATED_REFERANCE);
        return paimentFacture;
    }

    @BeforeEach
    public void initTest() {
        paimentFacture = createEntity(em);
    }

    @Test
    @Transactional
    void createPaimentFacture() throws Exception {
        int databaseSizeBeforeCreate = paimentFactureRepository.findAll().size();
        // Create the PaimentFacture
        PaimentFactureDTO paimentFactureDTO = paimentFactureMapper.toDto(paimentFacture);
        restPaimentFactureMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paimentFactureDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PaimentFacture in the database
        List<PaimentFacture> paimentFactureList = paimentFactureRepository.findAll();
        assertThat(paimentFactureList).hasSize(databaseSizeBeforeCreate + 1);
        PaimentFacture testPaimentFacture = paimentFactureList.get(paimentFactureList.size() - 1);
        assertThat(testPaimentFacture.getReferance()).isEqualTo(DEFAULT_REFERANCE);
    }

    @Test
    @Transactional
    void createPaimentFactureWithExistingId() throws Exception {
        // Create the PaimentFacture with an existing ID
        paimentFacture.setId(1L);
        PaimentFactureDTO paimentFactureDTO = paimentFactureMapper.toDto(paimentFacture);

        int databaseSizeBeforeCreate = paimentFactureRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaimentFactureMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paimentFactureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaimentFacture in the database
        List<PaimentFacture> paimentFactureList = paimentFactureRepository.findAll();
        assertThat(paimentFactureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkReferanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = paimentFactureRepository.findAll().size();
        // set the field null
        paimentFacture.setReferance(null);

        // Create the PaimentFacture, which fails.
        PaimentFactureDTO paimentFactureDTO = paimentFactureMapper.toDto(paimentFacture);

        restPaimentFactureMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paimentFactureDTO))
            )
            .andExpect(status().isBadRequest());

        List<PaimentFacture> paimentFactureList = paimentFactureRepository.findAll();
        assertThat(paimentFactureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPaimentFactures() throws Exception {
        // Initialize the database
        paimentFactureRepository.saveAndFlush(paimentFacture);

        // Get all the paimentFactureList
        restPaimentFactureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paimentFacture.getId().intValue())))
            .andExpect(jsonPath("$.[*].referance").value(hasItem(DEFAULT_REFERANCE.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPaimentFacturesWithEagerRelationshipsIsEnabled() throws Exception {
        when(paimentFactureServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPaimentFactureMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(paimentFactureServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPaimentFacturesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(paimentFactureServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPaimentFactureMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(paimentFactureServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getPaimentFacture() throws Exception {
        // Initialize the database
        paimentFactureRepository.saveAndFlush(paimentFacture);

        // Get the paimentFacture
        restPaimentFactureMockMvc
            .perform(get(ENTITY_API_URL_ID, paimentFacture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paimentFacture.getId().intValue()))
            .andExpect(jsonPath("$.referance").value(DEFAULT_REFERANCE.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingPaimentFacture() throws Exception {
        // Get the paimentFacture
        restPaimentFactureMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPaimentFacture() throws Exception {
        // Initialize the database
        paimentFactureRepository.saveAndFlush(paimentFacture);

        int databaseSizeBeforeUpdate = paimentFactureRepository.findAll().size();

        // Update the paimentFacture
        PaimentFacture updatedPaimentFacture = paimentFactureRepository.findById(paimentFacture.getId()).get();
        // Disconnect from session so that the updates on updatedPaimentFacture are not directly saved in db
        em.detach(updatedPaimentFacture);
        updatedPaimentFacture.referance(UPDATED_REFERANCE);
        PaimentFactureDTO paimentFactureDTO = paimentFactureMapper.toDto(updatedPaimentFacture);

        restPaimentFactureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paimentFactureDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paimentFactureDTO))
            )
            .andExpect(status().isOk());

        // Validate the PaimentFacture in the database
        List<PaimentFacture> paimentFactureList = paimentFactureRepository.findAll();
        assertThat(paimentFactureList).hasSize(databaseSizeBeforeUpdate);
        PaimentFacture testPaimentFacture = paimentFactureList.get(paimentFactureList.size() - 1);
        assertThat(testPaimentFacture.getReferance()).isEqualTo(UPDATED_REFERANCE);
    }

    @Test
    @Transactional
    void putNonExistingPaimentFacture() throws Exception {
        int databaseSizeBeforeUpdate = paimentFactureRepository.findAll().size();
        paimentFacture.setId(count.incrementAndGet());

        // Create the PaimentFacture
        PaimentFactureDTO paimentFactureDTO = paimentFactureMapper.toDto(paimentFacture);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaimentFactureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paimentFactureDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paimentFactureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaimentFacture in the database
        List<PaimentFacture> paimentFactureList = paimentFactureRepository.findAll();
        assertThat(paimentFactureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaimentFacture() throws Exception {
        int databaseSizeBeforeUpdate = paimentFactureRepository.findAll().size();
        paimentFacture.setId(count.incrementAndGet());

        // Create the PaimentFacture
        PaimentFactureDTO paimentFactureDTO = paimentFactureMapper.toDto(paimentFacture);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaimentFactureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paimentFactureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaimentFacture in the database
        List<PaimentFacture> paimentFactureList = paimentFactureRepository.findAll();
        assertThat(paimentFactureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaimentFacture() throws Exception {
        int databaseSizeBeforeUpdate = paimentFactureRepository.findAll().size();
        paimentFacture.setId(count.incrementAndGet());

        // Create the PaimentFacture
        PaimentFactureDTO paimentFactureDTO = paimentFactureMapper.toDto(paimentFacture);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaimentFactureMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paimentFactureDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaimentFacture in the database
        List<PaimentFacture> paimentFactureList = paimentFactureRepository.findAll();
        assertThat(paimentFactureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaimentFactureWithPatch() throws Exception {
        // Initialize the database
        paimentFactureRepository.saveAndFlush(paimentFacture);

        int databaseSizeBeforeUpdate = paimentFactureRepository.findAll().size();

        // Update the paimentFacture using partial update
        PaimentFacture partialUpdatedPaimentFacture = new PaimentFacture();
        partialUpdatedPaimentFacture.setId(paimentFacture.getId());

        partialUpdatedPaimentFacture.referance(UPDATED_REFERANCE);

        restPaimentFactureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaimentFacture.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaimentFacture))
            )
            .andExpect(status().isOk());

        // Validate the PaimentFacture in the database
        List<PaimentFacture> paimentFactureList = paimentFactureRepository.findAll();
        assertThat(paimentFactureList).hasSize(databaseSizeBeforeUpdate);
        PaimentFacture testPaimentFacture = paimentFactureList.get(paimentFactureList.size() - 1);
        assertThat(testPaimentFacture.getReferance()).isEqualTo(UPDATED_REFERANCE);
    }

    @Test
    @Transactional
    void fullUpdatePaimentFactureWithPatch() throws Exception {
        // Initialize the database
        paimentFactureRepository.saveAndFlush(paimentFacture);

        int databaseSizeBeforeUpdate = paimentFactureRepository.findAll().size();

        // Update the paimentFacture using partial update
        PaimentFacture partialUpdatedPaimentFacture = new PaimentFacture();
        partialUpdatedPaimentFacture.setId(paimentFacture.getId());

        partialUpdatedPaimentFacture.referance(UPDATED_REFERANCE);

        restPaimentFactureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaimentFacture.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaimentFacture))
            )
            .andExpect(status().isOk());

        // Validate the PaimentFacture in the database
        List<PaimentFacture> paimentFactureList = paimentFactureRepository.findAll();
        assertThat(paimentFactureList).hasSize(databaseSizeBeforeUpdate);
        PaimentFacture testPaimentFacture = paimentFactureList.get(paimentFactureList.size() - 1);
        assertThat(testPaimentFacture.getReferance()).isEqualTo(UPDATED_REFERANCE);
    }

    @Test
    @Transactional
    void patchNonExistingPaimentFacture() throws Exception {
        int databaseSizeBeforeUpdate = paimentFactureRepository.findAll().size();
        paimentFacture.setId(count.incrementAndGet());

        // Create the PaimentFacture
        PaimentFactureDTO paimentFactureDTO = paimentFactureMapper.toDto(paimentFacture);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaimentFactureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paimentFactureDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paimentFactureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaimentFacture in the database
        List<PaimentFacture> paimentFactureList = paimentFactureRepository.findAll();
        assertThat(paimentFactureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaimentFacture() throws Exception {
        int databaseSizeBeforeUpdate = paimentFactureRepository.findAll().size();
        paimentFacture.setId(count.incrementAndGet());

        // Create the PaimentFacture
        PaimentFactureDTO paimentFactureDTO = paimentFactureMapper.toDto(paimentFacture);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaimentFactureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paimentFactureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaimentFacture in the database
        List<PaimentFacture> paimentFactureList = paimentFactureRepository.findAll();
        assertThat(paimentFactureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaimentFacture() throws Exception {
        int databaseSizeBeforeUpdate = paimentFactureRepository.findAll().size();
        paimentFacture.setId(count.incrementAndGet());

        // Create the PaimentFacture
        PaimentFactureDTO paimentFactureDTO = paimentFactureMapper.toDto(paimentFacture);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaimentFactureMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paimentFactureDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaimentFacture in the database
        List<PaimentFacture> paimentFactureList = paimentFactureRepository.findAll();
        assertThat(paimentFactureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaimentFacture() throws Exception {
        // Initialize the database
        paimentFactureRepository.saveAndFlush(paimentFacture);

        int databaseSizeBeforeDelete = paimentFactureRepository.findAll().size();

        // Delete the paimentFacture
        restPaimentFactureMockMvc
            .perform(delete(ENTITY_API_URL_ID, paimentFacture.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PaimentFacture> paimentFactureList = paimentFactureRepository.findAll();
        assertThat(paimentFactureList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
