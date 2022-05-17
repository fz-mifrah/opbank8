package com.iga.opbank.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.iga.opbank.IntegrationTest;
import com.iga.opbank.domain.Operation;
import com.iga.opbank.domain.enumeration.EtatOperation;
import com.iga.opbank.domain.enumeration.TypeOperation;
import com.iga.opbank.repository.OperationRepository;
import com.iga.opbank.service.OperationService;
import com.iga.opbank.service.dto.OperationDTO;
import com.iga.opbank.service.mapper.OperationMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link OperationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OperationResourceIT {

    private static final String DEFAULT_NUM_OPERATION = "AAAAAAAAAA";
    private static final String UPDATED_NUM_OPERATION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final TypeOperation DEFAULT_TYPE_OPERATIN = TypeOperation.TypeVirement;
    private static final TypeOperation UPDATED_TYPE_OPERATIN = TypeOperation.TypeTransfer;

    private static final EtatOperation DEFAULT_ETAT_OPERATION = EtatOperation.Cree;
    private static final EtatOperation UPDATED_ETAT_OPERATION = EtatOperation.EnCours;

    private static final Double DEFAULT_MONTANT = 1D;
    private static final Double UPDATED_MONTANT = 2D;

    private static final String ENTITY_API_URL = "/api/operations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OperationRepository operationRepository;

    @Mock
    private OperationRepository operationRepositoryMock;

    @Autowired
    private OperationMapper operationMapper;

    @Mock
    private OperationService operationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOperationMockMvc;

    private Operation operation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Operation createEntity(EntityManager em) {
        Operation operation = new Operation()
            .numOperation(DEFAULT_NUM_OPERATION)
            .date(DEFAULT_DATE)
            .typeOperatin(DEFAULT_TYPE_OPERATIN)
            .etatOperation(DEFAULT_ETAT_OPERATION)
            .montant(DEFAULT_MONTANT);
        return operation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Operation createUpdatedEntity(EntityManager em) {
        Operation operation = new Operation()
            .numOperation(UPDATED_NUM_OPERATION)
            .date(UPDATED_DATE)
            .typeOperatin(UPDATED_TYPE_OPERATIN)
            .etatOperation(UPDATED_ETAT_OPERATION)
            .montant(UPDATED_MONTANT);
        return operation;
    }

    @BeforeEach
    public void initTest() {
        operation = createEntity(em);
    }

    @Test
    @Transactional
    void createOperation() throws Exception {
        int databaseSizeBeforeCreate = operationRepository.findAll().size();
        // Create the Operation
        OperationDTO operationDTO = operationMapper.toDto(operation);
        restOperationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operationDTO)))
            .andExpect(status().isCreated());

        // Validate the Operation in the database
        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeCreate + 1);
        Operation testOperation = operationList.get(operationList.size() - 1);
        assertThat(testOperation.getNumOperation()).isEqualTo(DEFAULT_NUM_OPERATION);
        assertThat(testOperation.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testOperation.getTypeOperatin()).isEqualTo(DEFAULT_TYPE_OPERATIN);
        assertThat(testOperation.getEtatOperation()).isEqualTo(DEFAULT_ETAT_OPERATION);
        assertThat(testOperation.getMontant()).isEqualTo(DEFAULT_MONTANT);
    }

    @Test
    @Transactional
    void createOperationWithExistingId() throws Exception {
        // Create the Operation with an existing ID
        operation.setId(1L);
        OperationDTO operationDTO = operationMapper.toDto(operation);

        int databaseSizeBeforeCreate = operationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOperationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Operation in the database
        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumOperationIsRequired() throws Exception {
        int databaseSizeBeforeTest = operationRepository.findAll().size();
        // set the field null
        operation.setNumOperation(null);

        // Create the Operation, which fails.
        OperationDTO operationDTO = operationMapper.toDto(operation);

        restOperationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operationDTO)))
            .andExpect(status().isBadRequest());

        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMontantIsRequired() throws Exception {
        int databaseSizeBeforeTest = operationRepository.findAll().size();
        // set the field null
        operation.setMontant(null);

        // Create the Operation, which fails.
        OperationDTO operationDTO = operationMapper.toDto(operation);

        restOperationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operationDTO)))
            .andExpect(status().isBadRequest());

        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOperations() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);

        // Get all the operationList
        restOperationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(operation.getId().intValue())))
            .andExpect(jsonPath("$.[*].numOperation").value(hasItem(DEFAULT_NUM_OPERATION)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].typeOperatin").value(hasItem(DEFAULT_TYPE_OPERATIN.toString())))
            .andExpect(jsonPath("$.[*].etatOperation").value(hasItem(DEFAULT_ETAT_OPERATION.toString())))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.doubleValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOperationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(operationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOperationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(operationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOperationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(operationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOperationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(operationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getOperation() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);

        // Get the operation
        restOperationMockMvc
            .perform(get(ENTITY_API_URL_ID, operation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(operation.getId().intValue()))
            .andExpect(jsonPath("$.numOperation").value(DEFAULT_NUM_OPERATION))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.typeOperatin").value(DEFAULT_TYPE_OPERATIN.toString()))
            .andExpect(jsonPath("$.etatOperation").value(DEFAULT_ETAT_OPERATION.toString()))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingOperation() throws Exception {
        // Get the operation
        restOperationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOperation() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);

        int databaseSizeBeforeUpdate = operationRepository.findAll().size();

        // Update the operation
        Operation updatedOperation = operationRepository.findById(operation.getId()).get();
        // Disconnect from session so that the updates on updatedOperation are not directly saved in db
        em.detach(updatedOperation);
        updatedOperation
            .numOperation(UPDATED_NUM_OPERATION)
            .date(UPDATED_DATE)
            .typeOperatin(UPDATED_TYPE_OPERATIN)
            .etatOperation(UPDATED_ETAT_OPERATION)
            .montant(UPDATED_MONTANT);
        OperationDTO operationDTO = operationMapper.toDto(updatedOperation);

        restOperationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, operationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Operation in the database
        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeUpdate);
        Operation testOperation = operationList.get(operationList.size() - 1);
        assertThat(testOperation.getNumOperation()).isEqualTo(UPDATED_NUM_OPERATION);
        assertThat(testOperation.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testOperation.getTypeOperatin()).isEqualTo(UPDATED_TYPE_OPERATIN);
        assertThat(testOperation.getEtatOperation()).isEqualTo(UPDATED_ETAT_OPERATION);
        assertThat(testOperation.getMontant()).isEqualTo(UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void putNonExistingOperation() throws Exception {
        int databaseSizeBeforeUpdate = operationRepository.findAll().size();
        operation.setId(count.incrementAndGet());

        // Create the Operation
        OperationDTO operationDTO = operationMapper.toDto(operation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, operationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operation in the database
        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOperation() throws Exception {
        int databaseSizeBeforeUpdate = operationRepository.findAll().size();
        operation.setId(count.incrementAndGet());

        // Create the Operation
        OperationDTO operationDTO = operationMapper.toDto(operation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operation in the database
        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOperation() throws Exception {
        int databaseSizeBeforeUpdate = operationRepository.findAll().size();
        operation.setId(count.incrementAndGet());

        // Create the Operation
        OperationDTO operationDTO = operationMapper.toDto(operation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Operation in the database
        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOperationWithPatch() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);

        int databaseSizeBeforeUpdate = operationRepository.findAll().size();

        // Update the operation using partial update
        Operation partialUpdatedOperation = new Operation();
        partialUpdatedOperation.setId(operation.getId());

        partialUpdatedOperation.etatOperation(UPDATED_ETAT_OPERATION);

        restOperationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOperation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOperation))
            )
            .andExpect(status().isOk());

        // Validate the Operation in the database
        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeUpdate);
        Operation testOperation = operationList.get(operationList.size() - 1);
        assertThat(testOperation.getNumOperation()).isEqualTo(DEFAULT_NUM_OPERATION);
        assertThat(testOperation.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testOperation.getTypeOperatin()).isEqualTo(DEFAULT_TYPE_OPERATIN);
        assertThat(testOperation.getEtatOperation()).isEqualTo(UPDATED_ETAT_OPERATION);
        assertThat(testOperation.getMontant()).isEqualTo(DEFAULT_MONTANT);
    }

    @Test
    @Transactional
    void fullUpdateOperationWithPatch() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);

        int databaseSizeBeforeUpdate = operationRepository.findAll().size();

        // Update the operation using partial update
        Operation partialUpdatedOperation = new Operation();
        partialUpdatedOperation.setId(operation.getId());

        partialUpdatedOperation
            .numOperation(UPDATED_NUM_OPERATION)
            .date(UPDATED_DATE)
            .typeOperatin(UPDATED_TYPE_OPERATIN)
            .etatOperation(UPDATED_ETAT_OPERATION)
            .montant(UPDATED_MONTANT);

        restOperationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOperation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOperation))
            )
            .andExpect(status().isOk());

        // Validate the Operation in the database
        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeUpdate);
        Operation testOperation = operationList.get(operationList.size() - 1);
        assertThat(testOperation.getNumOperation()).isEqualTo(UPDATED_NUM_OPERATION);
        assertThat(testOperation.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testOperation.getTypeOperatin()).isEqualTo(UPDATED_TYPE_OPERATIN);
        assertThat(testOperation.getEtatOperation()).isEqualTo(UPDATED_ETAT_OPERATION);
        assertThat(testOperation.getMontant()).isEqualTo(UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void patchNonExistingOperation() throws Exception {
        int databaseSizeBeforeUpdate = operationRepository.findAll().size();
        operation.setId(count.incrementAndGet());

        // Create the Operation
        OperationDTO operationDTO = operationMapper.toDto(operation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, operationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(operationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operation in the database
        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOperation() throws Exception {
        int databaseSizeBeforeUpdate = operationRepository.findAll().size();
        operation.setId(count.incrementAndGet());

        // Create the Operation
        OperationDTO operationDTO = operationMapper.toDto(operation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(operationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operation in the database
        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOperation() throws Exception {
        int databaseSizeBeforeUpdate = operationRepository.findAll().size();
        operation.setId(count.incrementAndGet());

        // Create the Operation
        OperationDTO operationDTO = operationMapper.toDto(operation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(operationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Operation in the database
        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOperation() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);

        int databaseSizeBeforeDelete = operationRepository.findAll().size();

        // Delete the operation
        restOperationMockMvc
            .perform(delete(ENTITY_API_URL_ID, operation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
