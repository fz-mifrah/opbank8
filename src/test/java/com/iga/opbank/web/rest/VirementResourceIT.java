package com.iga.opbank.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.iga.opbank.IntegrationTest;
import com.iga.opbank.domain.Virement;
import com.iga.opbank.repository.VirementRepository;
import com.iga.opbank.service.VirementService;
import com.iga.opbank.service.dto.VirementDTO;
import com.iga.opbank.service.mapper.VirementMapper;
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
 * Integration tests for the {@link VirementResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class VirementResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/virements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VirementRepository virementRepository;

    @Mock
    private VirementRepository virementRepositoryMock;

    @Autowired
    private VirementMapper virementMapper;

    @Mock
    private VirementService virementServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVirementMockMvc;

    private Virement virement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Virement createEntity(EntityManager em) {
        Virement virement = new Virement().description(DEFAULT_DESCRIPTION);
        return virement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Virement createUpdatedEntity(EntityManager em) {
        Virement virement = new Virement().description(UPDATED_DESCRIPTION);
        return virement;
    }

    @BeforeEach
    public void initTest() {
        virement = createEntity(em);
    }

    @Test
    @Transactional
    void createVirement() throws Exception {
        int databaseSizeBeforeCreate = virementRepository.findAll().size();
        // Create the Virement
        VirementDTO virementDTO = virementMapper.toDto(virement);
        restVirementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(virementDTO)))
            .andExpect(status().isCreated());

        // Validate the Virement in the database
        List<Virement> virementList = virementRepository.findAll();
        assertThat(virementList).hasSize(databaseSizeBeforeCreate + 1);
        Virement testVirement = virementList.get(virementList.size() - 1);
        assertThat(testVirement.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createVirementWithExistingId() throws Exception {
        // Create the Virement with an existing ID
        virement.setId(1L);
        VirementDTO virementDTO = virementMapper.toDto(virement);

        int databaseSizeBeforeCreate = virementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVirementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(virementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Virement in the database
        List<Virement> virementList = virementRepository.findAll();
        assertThat(virementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVirements() throws Exception {
        // Initialize the database
        virementRepository.saveAndFlush(virement);

        // Get all the virementList
        restVirementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(virement.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVirementsWithEagerRelationshipsIsEnabled() throws Exception {
        when(virementServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVirementMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(virementServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVirementsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(virementServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVirementMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(virementServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getVirement() throws Exception {
        // Initialize the database
        virementRepository.saveAndFlush(virement);

        // Get the virement
        restVirementMockMvc
            .perform(get(ENTITY_API_URL_ID, virement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(virement.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingVirement() throws Exception {
        // Get the virement
        restVirementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVirement() throws Exception {
        // Initialize the database
        virementRepository.saveAndFlush(virement);

        int databaseSizeBeforeUpdate = virementRepository.findAll().size();

        // Update the virement
        Virement updatedVirement = virementRepository.findById(virement.getId()).get();
        // Disconnect from session so that the updates on updatedVirement are not directly saved in db
        em.detach(updatedVirement);
        updatedVirement.description(UPDATED_DESCRIPTION);
        VirementDTO virementDTO = virementMapper.toDto(updatedVirement);

        restVirementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, virementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(virementDTO))
            )
            .andExpect(status().isOk());

        // Validate the Virement in the database
        List<Virement> virementList = virementRepository.findAll();
        assertThat(virementList).hasSize(databaseSizeBeforeUpdate);
        Virement testVirement = virementList.get(virementList.size() - 1);
        assertThat(testVirement.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingVirement() throws Exception {
        int databaseSizeBeforeUpdate = virementRepository.findAll().size();
        virement.setId(count.incrementAndGet());

        // Create the Virement
        VirementDTO virementDTO = virementMapper.toDto(virement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVirementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, virementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(virementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Virement in the database
        List<Virement> virementList = virementRepository.findAll();
        assertThat(virementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVirement() throws Exception {
        int databaseSizeBeforeUpdate = virementRepository.findAll().size();
        virement.setId(count.incrementAndGet());

        // Create the Virement
        VirementDTO virementDTO = virementMapper.toDto(virement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVirementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(virementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Virement in the database
        List<Virement> virementList = virementRepository.findAll();
        assertThat(virementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVirement() throws Exception {
        int databaseSizeBeforeUpdate = virementRepository.findAll().size();
        virement.setId(count.incrementAndGet());

        // Create the Virement
        VirementDTO virementDTO = virementMapper.toDto(virement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVirementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(virementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Virement in the database
        List<Virement> virementList = virementRepository.findAll();
        assertThat(virementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVirementWithPatch() throws Exception {
        // Initialize the database
        virementRepository.saveAndFlush(virement);

        int databaseSizeBeforeUpdate = virementRepository.findAll().size();

        // Update the virement using partial update
        Virement partialUpdatedVirement = new Virement();
        partialUpdatedVirement.setId(virement.getId());

        partialUpdatedVirement.description(UPDATED_DESCRIPTION);

        restVirementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVirement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVirement))
            )
            .andExpect(status().isOk());

        // Validate the Virement in the database
        List<Virement> virementList = virementRepository.findAll();
        assertThat(virementList).hasSize(databaseSizeBeforeUpdate);
        Virement testVirement = virementList.get(virementList.size() - 1);
        assertThat(testVirement.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateVirementWithPatch() throws Exception {
        // Initialize the database
        virementRepository.saveAndFlush(virement);

        int databaseSizeBeforeUpdate = virementRepository.findAll().size();

        // Update the virement using partial update
        Virement partialUpdatedVirement = new Virement();
        partialUpdatedVirement.setId(virement.getId());

        partialUpdatedVirement.description(UPDATED_DESCRIPTION);

        restVirementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVirement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVirement))
            )
            .andExpect(status().isOk());

        // Validate the Virement in the database
        List<Virement> virementList = virementRepository.findAll();
        assertThat(virementList).hasSize(databaseSizeBeforeUpdate);
        Virement testVirement = virementList.get(virementList.size() - 1);
        assertThat(testVirement.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingVirement() throws Exception {
        int databaseSizeBeforeUpdate = virementRepository.findAll().size();
        virement.setId(count.incrementAndGet());

        // Create the Virement
        VirementDTO virementDTO = virementMapper.toDto(virement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVirementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, virementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(virementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Virement in the database
        List<Virement> virementList = virementRepository.findAll();
        assertThat(virementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVirement() throws Exception {
        int databaseSizeBeforeUpdate = virementRepository.findAll().size();
        virement.setId(count.incrementAndGet());

        // Create the Virement
        VirementDTO virementDTO = virementMapper.toDto(virement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVirementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(virementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Virement in the database
        List<Virement> virementList = virementRepository.findAll();
        assertThat(virementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVirement() throws Exception {
        int databaseSizeBeforeUpdate = virementRepository.findAll().size();
        virement.setId(count.incrementAndGet());

        // Create the Virement
        VirementDTO virementDTO = virementMapper.toDto(virement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVirementMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(virementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Virement in the database
        List<Virement> virementList = virementRepository.findAll();
        assertThat(virementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVirement() throws Exception {
        // Initialize the database
        virementRepository.saveAndFlush(virement);

        int databaseSizeBeforeDelete = virementRepository.findAll().size();

        // Delete the virement
        restVirementMockMvc
            .perform(delete(ENTITY_API_URL_ID, virement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Virement> virementList = virementRepository.findAll();
        assertThat(virementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
