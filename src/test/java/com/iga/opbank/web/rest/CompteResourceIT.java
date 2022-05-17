package com.iga.opbank.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.iga.opbank.IntegrationTest;
import com.iga.opbank.domain.Compte;
import com.iga.opbank.repository.CompteRepository;
import com.iga.opbank.service.CompteService;
import com.iga.opbank.service.dto.CompteDTO;
import com.iga.opbank.service.mapper.CompteMapper;
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
 * Integration tests for the {@link CompteResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CompteResourceIT {

    private static final Long DEFAULT_RIB = 1L;
    private static final Long UPDATED_RIB = 2L;

    private static final LocalDate DEFAULT_DATE_OUVERTURE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OUVERTURE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_CODE = 1;
    private static final Integer UPDATED_CODE = 2;

    private static final Long DEFAULT_SOLDE = 1L;
    private static final Long UPDATED_SOLDE = 2L;

    private static final String ENTITY_API_URL = "/api/comptes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CompteRepository compteRepository;

    @Mock
    private CompteRepository compteRepositoryMock;

    @Autowired
    private CompteMapper compteMapper;

    @Mock
    private CompteService compteServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompteMockMvc;

    private Compte compte;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Compte createEntity(EntityManager em) {
        Compte compte = new Compte().rib(DEFAULT_RIB).dateOuverture(DEFAULT_DATE_OUVERTURE).code(DEFAULT_CODE).solde(DEFAULT_SOLDE);
        return compte;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Compte createUpdatedEntity(EntityManager em) {
        Compte compte = new Compte().rib(UPDATED_RIB).dateOuverture(UPDATED_DATE_OUVERTURE).code(UPDATED_CODE).solde(UPDATED_SOLDE);
        return compte;
    }

    @BeforeEach
    public void initTest() {
        compte = createEntity(em);
    }

    @Test
    @Transactional
    void createCompte() throws Exception {
        int databaseSizeBeforeCreate = compteRepository.findAll().size();
        // Create the Compte
        CompteDTO compteDTO = compteMapper.toDto(compte);
        restCompteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compteDTO)))
            .andExpect(status().isCreated());

        // Validate the Compte in the database
        List<Compte> compteList = compteRepository.findAll();
        assertThat(compteList).hasSize(databaseSizeBeforeCreate + 1);
        Compte testCompte = compteList.get(compteList.size() - 1);
        assertThat(testCompte.getRib()).isEqualTo(DEFAULT_RIB);
        assertThat(testCompte.getDateOuverture()).isEqualTo(DEFAULT_DATE_OUVERTURE);
        assertThat(testCompte.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCompte.getSolde()).isEqualTo(DEFAULT_SOLDE);
    }

    @Test
    @Transactional
    void createCompteWithExistingId() throws Exception {
        // Create the Compte with an existing ID
        compte.setId(1L);
        CompteDTO compteDTO = compteMapper.toDto(compte);

        int databaseSizeBeforeCreate = compteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Compte in the database
        List<Compte> compteList = compteRepository.findAll();
        assertThat(compteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRibIsRequired() throws Exception {
        int databaseSizeBeforeTest = compteRepository.findAll().size();
        // set the field null
        compte.setRib(null);

        // Create the Compte, which fails.
        CompteDTO compteDTO = compteMapper.toDto(compte);

        restCompteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compteDTO)))
            .andExpect(status().isBadRequest());

        List<Compte> compteList = compteRepository.findAll();
        assertThat(compteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = compteRepository.findAll().size();
        // set the field null
        compte.setCode(null);

        // Create the Compte, which fails.
        CompteDTO compteDTO = compteMapper.toDto(compte);

        restCompteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compteDTO)))
            .andExpect(status().isBadRequest());

        List<Compte> compteList = compteRepository.findAll();
        assertThat(compteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSoldeIsRequired() throws Exception {
        int databaseSizeBeforeTest = compteRepository.findAll().size();
        // set the field null
        compte.setSolde(null);

        // Create the Compte, which fails.
        CompteDTO compteDTO = compteMapper.toDto(compte);

        restCompteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compteDTO)))
            .andExpect(status().isBadRequest());

        List<Compte> compteList = compteRepository.findAll();
        assertThat(compteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllComptes() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);

        // Get all the compteList
        restCompteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compte.getId().intValue())))
            .andExpect(jsonPath("$.[*].rib").value(hasItem(DEFAULT_RIB.intValue())))
            .andExpect(jsonPath("$.[*].dateOuverture").value(hasItem(DEFAULT_DATE_OUVERTURE.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].solde").value(hasItem(DEFAULT_SOLDE.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllComptesWithEagerRelationshipsIsEnabled() throws Exception {
        when(compteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCompteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(compteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllComptesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(compteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCompteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(compteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getCompte() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);

        // Get the compte
        restCompteMockMvc
            .perform(get(ENTITY_API_URL_ID, compte.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(compte.getId().intValue()))
            .andExpect(jsonPath("$.rib").value(DEFAULT_RIB.intValue()))
            .andExpect(jsonPath("$.dateOuverture").value(DEFAULT_DATE_OUVERTURE.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.solde").value(DEFAULT_SOLDE.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingCompte() throws Exception {
        // Get the compte
        restCompteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCompte() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);

        int databaseSizeBeforeUpdate = compteRepository.findAll().size();

        // Update the compte
        Compte updatedCompte = compteRepository.findById(compte.getId()).get();
        // Disconnect from session so that the updates on updatedCompte are not directly saved in db
        em.detach(updatedCompte);
        updatedCompte.rib(UPDATED_RIB).dateOuverture(UPDATED_DATE_OUVERTURE).code(UPDATED_CODE).solde(UPDATED_SOLDE);
        CompteDTO compteDTO = compteMapper.toDto(updatedCompte);

        restCompteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, compteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(compteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Compte in the database
        List<Compte> compteList = compteRepository.findAll();
        assertThat(compteList).hasSize(databaseSizeBeforeUpdate);
        Compte testCompte = compteList.get(compteList.size() - 1);
        assertThat(testCompte.getRib()).isEqualTo(UPDATED_RIB);
        assertThat(testCompte.getDateOuverture()).isEqualTo(UPDATED_DATE_OUVERTURE);
        assertThat(testCompte.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCompte.getSolde()).isEqualTo(UPDATED_SOLDE);
    }

    @Test
    @Transactional
    void putNonExistingCompte() throws Exception {
        int databaseSizeBeforeUpdate = compteRepository.findAll().size();
        compte.setId(count.incrementAndGet());

        // Create the Compte
        CompteDTO compteDTO = compteMapper.toDto(compte);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, compteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(compteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compte in the database
        List<Compte> compteList = compteRepository.findAll();
        assertThat(compteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompte() throws Exception {
        int databaseSizeBeforeUpdate = compteRepository.findAll().size();
        compte.setId(count.incrementAndGet());

        // Create the Compte
        CompteDTO compteDTO = compteMapper.toDto(compte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(compteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compte in the database
        List<Compte> compteList = compteRepository.findAll();
        assertThat(compteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompte() throws Exception {
        int databaseSizeBeforeUpdate = compteRepository.findAll().size();
        compte.setId(count.incrementAndGet());

        // Create the Compte
        CompteDTO compteDTO = compteMapper.toDto(compte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Compte in the database
        List<Compte> compteList = compteRepository.findAll();
        assertThat(compteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompteWithPatch() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);

        int databaseSizeBeforeUpdate = compteRepository.findAll().size();

        // Update the compte using partial update
        Compte partialUpdatedCompte = new Compte();
        partialUpdatedCompte.setId(compte.getId());

        partialUpdatedCompte.rib(UPDATED_RIB).solde(UPDATED_SOLDE);

        restCompteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompte.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompte))
            )
            .andExpect(status().isOk());

        // Validate the Compte in the database
        List<Compte> compteList = compteRepository.findAll();
        assertThat(compteList).hasSize(databaseSizeBeforeUpdate);
        Compte testCompte = compteList.get(compteList.size() - 1);
        assertThat(testCompte.getRib()).isEqualTo(UPDATED_RIB);
        assertThat(testCompte.getDateOuverture()).isEqualTo(DEFAULT_DATE_OUVERTURE);
        assertThat(testCompte.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCompte.getSolde()).isEqualTo(UPDATED_SOLDE);
    }

    @Test
    @Transactional
    void fullUpdateCompteWithPatch() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);

        int databaseSizeBeforeUpdate = compteRepository.findAll().size();

        // Update the compte using partial update
        Compte partialUpdatedCompte = new Compte();
        partialUpdatedCompte.setId(compte.getId());

        partialUpdatedCompte.rib(UPDATED_RIB).dateOuverture(UPDATED_DATE_OUVERTURE).code(UPDATED_CODE).solde(UPDATED_SOLDE);

        restCompteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompte.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompte))
            )
            .andExpect(status().isOk());

        // Validate the Compte in the database
        List<Compte> compteList = compteRepository.findAll();
        assertThat(compteList).hasSize(databaseSizeBeforeUpdate);
        Compte testCompte = compteList.get(compteList.size() - 1);
        assertThat(testCompte.getRib()).isEqualTo(UPDATED_RIB);
        assertThat(testCompte.getDateOuverture()).isEqualTo(UPDATED_DATE_OUVERTURE);
        assertThat(testCompte.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCompte.getSolde()).isEqualTo(UPDATED_SOLDE);
    }

    @Test
    @Transactional
    void patchNonExistingCompte() throws Exception {
        int databaseSizeBeforeUpdate = compteRepository.findAll().size();
        compte.setId(count.incrementAndGet());

        // Create the Compte
        CompteDTO compteDTO = compteMapper.toDto(compte);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, compteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(compteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compte in the database
        List<Compte> compteList = compteRepository.findAll();
        assertThat(compteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompte() throws Exception {
        int databaseSizeBeforeUpdate = compteRepository.findAll().size();
        compte.setId(count.incrementAndGet());

        // Create the Compte
        CompteDTO compteDTO = compteMapper.toDto(compte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(compteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compte in the database
        List<Compte> compteList = compteRepository.findAll();
        assertThat(compteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompte() throws Exception {
        int databaseSizeBeforeUpdate = compteRepository.findAll().size();
        compte.setId(count.incrementAndGet());

        // Create the Compte
        CompteDTO compteDTO = compteMapper.toDto(compte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompteMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(compteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Compte in the database
        List<Compte> compteList = compteRepository.findAll();
        assertThat(compteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompte() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);

        int databaseSizeBeforeDelete = compteRepository.findAll().size();

        // Delete the compte
        restCompteMockMvc
            .perform(delete(ENTITY_API_URL_ID, compte.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Compte> compteList = compteRepository.findAll();
        assertThat(compteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
