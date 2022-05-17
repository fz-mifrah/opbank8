package com.iga.opbank.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.iga.opbank.IntegrationTest;
import com.iga.opbank.domain.Transfer;
import com.iga.opbank.repository.TransferRepository;
import com.iga.opbank.service.dto.TransferDTO;
import com.iga.opbank.service.mapper.TransferMapper;
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
 * Integration tests for the {@link TransferResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TransferResourceIT {

    private static final String DEFAULT_CIN = "AAAAAAAAAA";
    private static final String UPDATED_CIN = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM_PRENOM = "BBBBBBBBBB";

    private static final Long DEFAULT_TEL = 1L;
    private static final Long UPDATED_TEL = 2L;

    private static final String ENTITY_API_URL = "/api/transfers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private TransferMapper transferMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransferMockMvc;

    private Transfer transfer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transfer createEntity(EntityManager em) {
        Transfer transfer = new Transfer().cin(DEFAULT_CIN).nomPrenom(DEFAULT_NOM_PRENOM).tel(DEFAULT_TEL);
        return transfer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transfer createUpdatedEntity(EntityManager em) {
        Transfer transfer = new Transfer().cin(UPDATED_CIN).nomPrenom(UPDATED_NOM_PRENOM).tel(UPDATED_TEL);
        return transfer;
    }

    @BeforeEach
    public void initTest() {
        transfer = createEntity(em);
    }

    @Test
    @Transactional
    void createTransfer() throws Exception {
        int databaseSizeBeforeCreate = transferRepository.findAll().size();
        // Create the Transfer
        TransferDTO transferDTO = transferMapper.toDto(transfer);
        restTransferMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transferDTO)))
            .andExpect(status().isCreated());

        // Validate the Transfer in the database
        List<Transfer> transferList = transferRepository.findAll();
        assertThat(transferList).hasSize(databaseSizeBeforeCreate + 1);
        Transfer testTransfer = transferList.get(transferList.size() - 1);
        assertThat(testTransfer.getCin()).isEqualTo(DEFAULT_CIN);
        assertThat(testTransfer.getNomPrenom()).isEqualTo(DEFAULT_NOM_PRENOM);
        assertThat(testTransfer.getTel()).isEqualTo(DEFAULT_TEL);
    }

    @Test
    @Transactional
    void createTransferWithExistingId() throws Exception {
        // Create the Transfer with an existing ID
        transfer.setId(1L);
        TransferDTO transferDTO = transferMapper.toDto(transfer);

        int databaseSizeBeforeCreate = transferRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransferMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transferDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Transfer in the database
        List<Transfer> transferList = transferRepository.findAll();
        assertThat(transferList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCinIsRequired() throws Exception {
        int databaseSizeBeforeTest = transferRepository.findAll().size();
        // set the field null
        transfer.setCin(null);

        // Create the Transfer, which fails.
        TransferDTO transferDTO = transferMapper.toDto(transfer);

        restTransferMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transferDTO)))
            .andExpect(status().isBadRequest());

        List<Transfer> transferList = transferRepository.findAll();
        assertThat(transferList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomPrenomIsRequired() throws Exception {
        int databaseSizeBeforeTest = transferRepository.findAll().size();
        // set the field null
        transfer.setNomPrenom(null);

        // Create the Transfer, which fails.
        TransferDTO transferDTO = transferMapper.toDto(transfer);

        restTransferMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transferDTO)))
            .andExpect(status().isBadRequest());

        List<Transfer> transferList = transferRepository.findAll();
        assertThat(transferList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTransfers() throws Exception {
        // Initialize the database
        transferRepository.saveAndFlush(transfer);

        // Get all the transferList
        restTransferMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transfer.getId().intValue())))
            .andExpect(jsonPath("$.[*].cin").value(hasItem(DEFAULT_CIN)))
            .andExpect(jsonPath("$.[*].nomPrenom").value(hasItem(DEFAULT_NOM_PRENOM)))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL.intValue())));
    }

    @Test
    @Transactional
    void getTransfer() throws Exception {
        // Initialize the database
        transferRepository.saveAndFlush(transfer);

        // Get the transfer
        restTransferMockMvc
            .perform(get(ENTITY_API_URL_ID, transfer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transfer.getId().intValue()))
            .andExpect(jsonPath("$.cin").value(DEFAULT_CIN))
            .andExpect(jsonPath("$.nomPrenom").value(DEFAULT_NOM_PRENOM))
            .andExpect(jsonPath("$.tel").value(DEFAULT_TEL.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingTransfer() throws Exception {
        // Get the transfer
        restTransferMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTransfer() throws Exception {
        // Initialize the database
        transferRepository.saveAndFlush(transfer);

        int databaseSizeBeforeUpdate = transferRepository.findAll().size();

        // Update the transfer
        Transfer updatedTransfer = transferRepository.findById(transfer.getId()).get();
        // Disconnect from session so that the updates on updatedTransfer are not directly saved in db
        em.detach(updatedTransfer);
        updatedTransfer.cin(UPDATED_CIN).nomPrenom(UPDATED_NOM_PRENOM).tel(UPDATED_TEL);
        TransferDTO transferDTO = transferMapper.toDto(updatedTransfer);

        restTransferMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transferDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transferDTO))
            )
            .andExpect(status().isOk());

        // Validate the Transfer in the database
        List<Transfer> transferList = transferRepository.findAll();
        assertThat(transferList).hasSize(databaseSizeBeforeUpdate);
        Transfer testTransfer = transferList.get(transferList.size() - 1);
        assertThat(testTransfer.getCin()).isEqualTo(UPDATED_CIN);
        assertThat(testTransfer.getNomPrenom()).isEqualTo(UPDATED_NOM_PRENOM);
        assertThat(testTransfer.getTel()).isEqualTo(UPDATED_TEL);
    }

    @Test
    @Transactional
    void putNonExistingTransfer() throws Exception {
        int databaseSizeBeforeUpdate = transferRepository.findAll().size();
        transfer.setId(count.incrementAndGet());

        // Create the Transfer
        TransferDTO transferDTO = transferMapper.toDto(transfer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransferMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transferDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transferDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transfer in the database
        List<Transfer> transferList = transferRepository.findAll();
        assertThat(transferList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransfer() throws Exception {
        int databaseSizeBeforeUpdate = transferRepository.findAll().size();
        transfer.setId(count.incrementAndGet());

        // Create the Transfer
        TransferDTO transferDTO = transferMapper.toDto(transfer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransferMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transferDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transfer in the database
        List<Transfer> transferList = transferRepository.findAll();
        assertThat(transferList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransfer() throws Exception {
        int databaseSizeBeforeUpdate = transferRepository.findAll().size();
        transfer.setId(count.incrementAndGet());

        // Create the Transfer
        TransferDTO transferDTO = transferMapper.toDto(transfer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransferMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transferDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transfer in the database
        List<Transfer> transferList = transferRepository.findAll();
        assertThat(transferList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransferWithPatch() throws Exception {
        // Initialize the database
        transferRepository.saveAndFlush(transfer);

        int databaseSizeBeforeUpdate = transferRepository.findAll().size();

        // Update the transfer using partial update
        Transfer partialUpdatedTransfer = new Transfer();
        partialUpdatedTransfer.setId(transfer.getId());

        partialUpdatedTransfer.cin(UPDATED_CIN).tel(UPDATED_TEL);

        restTransferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransfer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransfer))
            )
            .andExpect(status().isOk());

        // Validate the Transfer in the database
        List<Transfer> transferList = transferRepository.findAll();
        assertThat(transferList).hasSize(databaseSizeBeforeUpdate);
        Transfer testTransfer = transferList.get(transferList.size() - 1);
        assertThat(testTransfer.getCin()).isEqualTo(UPDATED_CIN);
        assertThat(testTransfer.getNomPrenom()).isEqualTo(DEFAULT_NOM_PRENOM);
        assertThat(testTransfer.getTel()).isEqualTo(UPDATED_TEL);
    }

    @Test
    @Transactional
    void fullUpdateTransferWithPatch() throws Exception {
        // Initialize the database
        transferRepository.saveAndFlush(transfer);

        int databaseSizeBeforeUpdate = transferRepository.findAll().size();

        // Update the transfer using partial update
        Transfer partialUpdatedTransfer = new Transfer();
        partialUpdatedTransfer.setId(transfer.getId());

        partialUpdatedTransfer.cin(UPDATED_CIN).nomPrenom(UPDATED_NOM_PRENOM).tel(UPDATED_TEL);

        restTransferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransfer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransfer))
            )
            .andExpect(status().isOk());

        // Validate the Transfer in the database
        List<Transfer> transferList = transferRepository.findAll();
        assertThat(transferList).hasSize(databaseSizeBeforeUpdate);
        Transfer testTransfer = transferList.get(transferList.size() - 1);
        assertThat(testTransfer.getCin()).isEqualTo(UPDATED_CIN);
        assertThat(testTransfer.getNomPrenom()).isEqualTo(UPDATED_NOM_PRENOM);
        assertThat(testTransfer.getTel()).isEqualTo(UPDATED_TEL);
    }

    @Test
    @Transactional
    void patchNonExistingTransfer() throws Exception {
        int databaseSizeBeforeUpdate = transferRepository.findAll().size();
        transfer.setId(count.incrementAndGet());

        // Create the Transfer
        TransferDTO transferDTO = transferMapper.toDto(transfer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transferDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transferDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transfer in the database
        List<Transfer> transferList = transferRepository.findAll();
        assertThat(transferList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransfer() throws Exception {
        int databaseSizeBeforeUpdate = transferRepository.findAll().size();
        transfer.setId(count.incrementAndGet());

        // Create the Transfer
        TransferDTO transferDTO = transferMapper.toDto(transfer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transferDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transfer in the database
        List<Transfer> transferList = transferRepository.findAll();
        assertThat(transferList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransfer() throws Exception {
        int databaseSizeBeforeUpdate = transferRepository.findAll().size();
        transfer.setId(count.incrementAndGet());

        // Create the Transfer
        TransferDTO transferDTO = transferMapper.toDto(transfer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransferMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(transferDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transfer in the database
        List<Transfer> transferList = transferRepository.findAll();
        assertThat(transferList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransfer() throws Exception {
        // Initialize the database
        transferRepository.saveAndFlush(transfer);

        int databaseSizeBeforeDelete = transferRepository.findAll().size();

        // Delete the transfer
        restTransferMockMvc
            .perform(delete(ENTITY_API_URL_ID, transfer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Transfer> transferList = transferRepository.findAll();
        assertThat(transferList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
