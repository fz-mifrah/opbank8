package com.iga.opbank.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.iga.opbank.IntegrationTest;
import com.iga.opbank.domain.Recharge;
import com.iga.opbank.repository.RechargeRepository;
import com.iga.opbank.service.RechargeService;
import com.iga.opbank.service.dto.RechargeDTO;
import com.iga.opbank.service.mapper.RechargeMapper;
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
 * Integration tests for the {@link RechargeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RechargeResourceIT {

    private static final Long DEFAULT_NUM_TEL = 1L;
    private static final Long UPDATED_NUM_TEL = 2L;

    private static final String ENTITY_API_URL = "/api/recharges";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RechargeRepository rechargeRepository;

    @Mock
    private RechargeRepository rechargeRepositoryMock;

    @Autowired
    private RechargeMapper rechargeMapper;

    @Mock
    private RechargeService rechargeServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRechargeMockMvc;

    private Recharge recharge;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Recharge createEntity(EntityManager em) {
        Recharge recharge = new Recharge().numTel(DEFAULT_NUM_TEL);
        return recharge;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Recharge createUpdatedEntity(EntityManager em) {
        Recharge recharge = new Recharge().numTel(UPDATED_NUM_TEL);
        return recharge;
    }

    @BeforeEach
    public void initTest() {
        recharge = createEntity(em);
    }

    @Test
    @Transactional
    void createRecharge() throws Exception {
        int databaseSizeBeforeCreate = rechargeRepository.findAll().size();
        // Create the Recharge
        RechargeDTO rechargeDTO = rechargeMapper.toDto(recharge);
        restRechargeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rechargeDTO)))
            .andExpect(status().isCreated());

        // Validate the Recharge in the database
        List<Recharge> rechargeList = rechargeRepository.findAll();
        assertThat(rechargeList).hasSize(databaseSizeBeforeCreate + 1);
        Recharge testRecharge = rechargeList.get(rechargeList.size() - 1);
        assertThat(testRecharge.getNumTel()).isEqualTo(DEFAULT_NUM_TEL);
    }

    @Test
    @Transactional
    void createRechargeWithExistingId() throws Exception {
        // Create the Recharge with an existing ID
        recharge.setId(1L);
        RechargeDTO rechargeDTO = rechargeMapper.toDto(recharge);

        int databaseSizeBeforeCreate = rechargeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRechargeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rechargeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Recharge in the database
        List<Recharge> rechargeList = rechargeRepository.findAll();
        assertThat(rechargeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumTelIsRequired() throws Exception {
        int databaseSizeBeforeTest = rechargeRepository.findAll().size();
        // set the field null
        recharge.setNumTel(null);

        // Create the Recharge, which fails.
        RechargeDTO rechargeDTO = rechargeMapper.toDto(recharge);

        restRechargeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rechargeDTO)))
            .andExpect(status().isBadRequest());

        List<Recharge> rechargeList = rechargeRepository.findAll();
        assertThat(rechargeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRecharges() throws Exception {
        // Initialize the database
        rechargeRepository.saveAndFlush(recharge);

        // Get all the rechargeList
        restRechargeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recharge.getId().intValue())))
            .andExpect(jsonPath("$.[*].numTel").value(hasItem(DEFAULT_NUM_TEL.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRechargesWithEagerRelationshipsIsEnabled() throws Exception {
        when(rechargeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRechargeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(rechargeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRechargesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(rechargeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRechargeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(rechargeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getRecharge() throws Exception {
        // Initialize the database
        rechargeRepository.saveAndFlush(recharge);

        // Get the recharge
        restRechargeMockMvc
            .perform(get(ENTITY_API_URL_ID, recharge.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(recharge.getId().intValue()))
            .andExpect(jsonPath("$.numTel").value(DEFAULT_NUM_TEL.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingRecharge() throws Exception {
        // Get the recharge
        restRechargeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRecharge() throws Exception {
        // Initialize the database
        rechargeRepository.saveAndFlush(recharge);

        int databaseSizeBeforeUpdate = rechargeRepository.findAll().size();

        // Update the recharge
        Recharge updatedRecharge = rechargeRepository.findById(recharge.getId()).get();
        // Disconnect from session so that the updates on updatedRecharge are not directly saved in db
        em.detach(updatedRecharge);
        updatedRecharge.numTel(UPDATED_NUM_TEL);
        RechargeDTO rechargeDTO = rechargeMapper.toDto(updatedRecharge);

        restRechargeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rechargeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rechargeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Recharge in the database
        List<Recharge> rechargeList = rechargeRepository.findAll();
        assertThat(rechargeList).hasSize(databaseSizeBeforeUpdate);
        Recharge testRecharge = rechargeList.get(rechargeList.size() - 1);
        assertThat(testRecharge.getNumTel()).isEqualTo(UPDATED_NUM_TEL);
    }

    @Test
    @Transactional
    void putNonExistingRecharge() throws Exception {
        int databaseSizeBeforeUpdate = rechargeRepository.findAll().size();
        recharge.setId(count.incrementAndGet());

        // Create the Recharge
        RechargeDTO rechargeDTO = rechargeMapper.toDto(recharge);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRechargeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rechargeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rechargeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recharge in the database
        List<Recharge> rechargeList = rechargeRepository.findAll();
        assertThat(rechargeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRecharge() throws Exception {
        int databaseSizeBeforeUpdate = rechargeRepository.findAll().size();
        recharge.setId(count.incrementAndGet());

        // Create the Recharge
        RechargeDTO rechargeDTO = rechargeMapper.toDto(recharge);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRechargeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rechargeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recharge in the database
        List<Recharge> rechargeList = rechargeRepository.findAll();
        assertThat(rechargeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRecharge() throws Exception {
        int databaseSizeBeforeUpdate = rechargeRepository.findAll().size();
        recharge.setId(count.incrementAndGet());

        // Create the Recharge
        RechargeDTO rechargeDTO = rechargeMapper.toDto(recharge);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRechargeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rechargeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Recharge in the database
        List<Recharge> rechargeList = rechargeRepository.findAll();
        assertThat(rechargeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRechargeWithPatch() throws Exception {
        // Initialize the database
        rechargeRepository.saveAndFlush(recharge);

        int databaseSizeBeforeUpdate = rechargeRepository.findAll().size();

        // Update the recharge using partial update
        Recharge partialUpdatedRecharge = new Recharge();
        partialUpdatedRecharge.setId(recharge.getId());

        restRechargeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecharge.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRecharge))
            )
            .andExpect(status().isOk());

        // Validate the Recharge in the database
        List<Recharge> rechargeList = rechargeRepository.findAll();
        assertThat(rechargeList).hasSize(databaseSizeBeforeUpdate);
        Recharge testRecharge = rechargeList.get(rechargeList.size() - 1);
        assertThat(testRecharge.getNumTel()).isEqualTo(DEFAULT_NUM_TEL);
    }

    @Test
    @Transactional
    void fullUpdateRechargeWithPatch() throws Exception {
        // Initialize the database
        rechargeRepository.saveAndFlush(recharge);

        int databaseSizeBeforeUpdate = rechargeRepository.findAll().size();

        // Update the recharge using partial update
        Recharge partialUpdatedRecharge = new Recharge();
        partialUpdatedRecharge.setId(recharge.getId());

        partialUpdatedRecharge.numTel(UPDATED_NUM_TEL);

        restRechargeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecharge.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRecharge))
            )
            .andExpect(status().isOk());

        // Validate the Recharge in the database
        List<Recharge> rechargeList = rechargeRepository.findAll();
        assertThat(rechargeList).hasSize(databaseSizeBeforeUpdate);
        Recharge testRecharge = rechargeList.get(rechargeList.size() - 1);
        assertThat(testRecharge.getNumTel()).isEqualTo(UPDATED_NUM_TEL);
    }

    @Test
    @Transactional
    void patchNonExistingRecharge() throws Exception {
        int databaseSizeBeforeUpdate = rechargeRepository.findAll().size();
        recharge.setId(count.incrementAndGet());

        // Create the Recharge
        RechargeDTO rechargeDTO = rechargeMapper.toDto(recharge);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRechargeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rechargeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rechargeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recharge in the database
        List<Recharge> rechargeList = rechargeRepository.findAll();
        assertThat(rechargeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRecharge() throws Exception {
        int databaseSizeBeforeUpdate = rechargeRepository.findAll().size();
        recharge.setId(count.incrementAndGet());

        // Create the Recharge
        RechargeDTO rechargeDTO = rechargeMapper.toDto(recharge);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRechargeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rechargeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recharge in the database
        List<Recharge> rechargeList = rechargeRepository.findAll();
        assertThat(rechargeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRecharge() throws Exception {
        int databaseSizeBeforeUpdate = rechargeRepository.findAll().size();
        recharge.setId(count.incrementAndGet());

        // Create the Recharge
        RechargeDTO rechargeDTO = rechargeMapper.toDto(recharge);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRechargeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(rechargeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Recharge in the database
        List<Recharge> rechargeList = rechargeRepository.findAll();
        assertThat(rechargeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRecharge() throws Exception {
        // Initialize the database
        rechargeRepository.saveAndFlush(recharge);

        int databaseSizeBeforeDelete = rechargeRepository.findAll().size();

        // Delete the recharge
        restRechargeMockMvc
            .perform(delete(ENTITY_API_URL_ID, recharge.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Recharge> rechargeList = rechargeRepository.findAll();
        assertThat(rechargeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
