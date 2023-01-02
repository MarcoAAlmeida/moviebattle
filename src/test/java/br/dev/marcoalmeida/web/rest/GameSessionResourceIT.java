package br.dev.marcoalmeida.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.dev.marcoalmeida.IntegrationTest;
import br.dev.marcoalmeida.domain.GameSession;
import br.dev.marcoalmeida.repository.GameSessionRepository;
import java.util.List;
import java.util.UUID;
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
 * Integration tests for the {@link GameSessionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GameSessionResourceIT {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final String ENTITY_API_URL = "/api/game-sessions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private GameSessionRepository gameSessionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGameSessionMockMvc;

    private GameSession gameSession;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GameSession createEntity(EntityManager em) {
        GameSession gameSession = new GameSession().userId(DEFAULT_USER_ID);
        return gameSession;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GameSession createUpdatedEntity(EntityManager em) {
        GameSession gameSession = new GameSession().userId(UPDATED_USER_ID);
        return gameSession;
    }

    @BeforeEach
    public void initTest() {
        gameSession = createEntity(em);
    }

    @Test
    @Transactional
    void createGameSession() throws Exception {
        int databaseSizeBeforeCreate = gameSessionRepository.findAll().size();
        // Create the GameSession
        restGameSessionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameSession))
            )
            .andExpect(status().isCreated());

        // Validate the GameSession in the database
        List<GameSession> gameSessionList = gameSessionRepository.findAll();
        assertThat(gameSessionList).hasSize(databaseSizeBeforeCreate + 1);
        GameSession testGameSession = gameSessionList.get(gameSessionList.size() - 1);
        assertThat(testGameSession.getUserId()).isEqualTo(DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    void createGameSessionWithExistingId() throws Exception {
        // Create the GameSession with an existing ID
        gameSession.setId("existing_id");

        int databaseSizeBeforeCreate = gameSessionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGameSessionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameSession))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameSession in the database
        List<GameSession> gameSessionList = gameSessionRepository.findAll();
        assertThat(gameSessionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGameSessions() throws Exception {
        // Initialize the database
        gameSession.setId(UUID.randomUUID().toString());
        gameSessionRepository.saveAndFlush(gameSession);

        // Get all the gameSessionList
        restGameSessionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gameSession.getId())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())));
    }

    @Test
    @Transactional
    void getGameSession() throws Exception {
        // Initialize the database
        gameSession.setId(UUID.randomUUID().toString());
        gameSessionRepository.saveAndFlush(gameSession);

        // Get the gameSession
        restGameSessionMockMvc
            .perform(get(ENTITY_API_URL_ID, gameSession.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gameSession.getId()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingGameSession() throws Exception {
        // Get the gameSession
        restGameSessionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGameSession() throws Exception {
        // Initialize the database
        gameSession.setId(UUID.randomUUID().toString());
        gameSessionRepository.saveAndFlush(gameSession);

        int databaseSizeBeforeUpdate = gameSessionRepository.findAll().size();

        // Update the gameSession
        GameSession updatedGameSession = gameSessionRepository.findById(gameSession.getId()).get();
        // Disconnect from session so that the updates on updatedGameSession are not directly saved in db
        em.detach(updatedGameSession);
        updatedGameSession.userId(UPDATED_USER_ID);

        restGameSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGameSession.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGameSession))
            )
            .andExpect(status().isOk());

        // Validate the GameSession in the database
        List<GameSession> gameSessionList = gameSessionRepository.findAll();
        assertThat(gameSessionList).hasSize(databaseSizeBeforeUpdate);
        GameSession testGameSession = gameSessionList.get(gameSessionList.size() - 1);
        assertThat(testGameSession.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void putNonExistingGameSession() throws Exception {
        int databaseSizeBeforeUpdate = gameSessionRepository.findAll().size();
        gameSession.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGameSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gameSession.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameSession))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameSession in the database
        List<GameSession> gameSessionList = gameSessionRepository.findAll();
        assertThat(gameSessionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGameSession() throws Exception {
        int databaseSizeBeforeUpdate = gameSessionRepository.findAll().size();
        gameSession.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameSession))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameSession in the database
        List<GameSession> gameSessionList = gameSessionRepository.findAll();
        assertThat(gameSessionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGameSession() throws Exception {
        int databaseSizeBeforeUpdate = gameSessionRepository.findAll().size();
        gameSession.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameSessionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameSession))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GameSession in the database
        List<GameSession> gameSessionList = gameSessionRepository.findAll();
        assertThat(gameSessionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGameSessionWithPatch() throws Exception {
        // Initialize the database
        gameSession.setId(UUID.randomUUID().toString());
        gameSessionRepository.saveAndFlush(gameSession);

        int databaseSizeBeforeUpdate = gameSessionRepository.findAll().size();

        // Update the gameSession using partial update
        GameSession partialUpdatedGameSession = new GameSession();
        partialUpdatedGameSession.setId(gameSession.getId());

        partialUpdatedGameSession.userId(UPDATED_USER_ID);

        restGameSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGameSession.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGameSession))
            )
            .andExpect(status().isOk());

        // Validate the GameSession in the database
        List<GameSession> gameSessionList = gameSessionRepository.findAll();
        assertThat(gameSessionList).hasSize(databaseSizeBeforeUpdate);
        GameSession testGameSession = gameSessionList.get(gameSessionList.size() - 1);
        assertThat(testGameSession.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void fullUpdateGameSessionWithPatch() throws Exception {
        // Initialize the database
        gameSession.setId(UUID.randomUUID().toString());
        gameSessionRepository.saveAndFlush(gameSession);

        int databaseSizeBeforeUpdate = gameSessionRepository.findAll().size();

        // Update the gameSession using partial update
        GameSession partialUpdatedGameSession = new GameSession();
        partialUpdatedGameSession.setId(gameSession.getId());

        partialUpdatedGameSession.userId(UPDATED_USER_ID);

        restGameSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGameSession.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGameSession))
            )
            .andExpect(status().isOk());

        // Validate the GameSession in the database
        List<GameSession> gameSessionList = gameSessionRepository.findAll();
        assertThat(gameSessionList).hasSize(databaseSizeBeforeUpdate);
        GameSession testGameSession = gameSessionList.get(gameSessionList.size() - 1);
        assertThat(testGameSession.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void patchNonExistingGameSession() throws Exception {
        int databaseSizeBeforeUpdate = gameSessionRepository.findAll().size();
        gameSession.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGameSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gameSession.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gameSession))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameSession in the database
        List<GameSession> gameSessionList = gameSessionRepository.findAll();
        assertThat(gameSessionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGameSession() throws Exception {
        int databaseSizeBeforeUpdate = gameSessionRepository.findAll().size();
        gameSession.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gameSession))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameSession in the database
        List<GameSession> gameSessionList = gameSessionRepository.findAll();
        assertThat(gameSessionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGameSession() throws Exception {
        int databaseSizeBeforeUpdate = gameSessionRepository.findAll().size();
        gameSession.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameSessionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gameSession))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GameSession in the database
        List<GameSession> gameSessionList = gameSessionRepository.findAll();
        assertThat(gameSessionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGameSession() throws Exception {
        // Initialize the database
        gameSession.setId(UUID.randomUUID().toString());
        gameSessionRepository.saveAndFlush(gameSession);

        int databaseSizeBeforeDelete = gameSessionRepository.findAll().size();

        // Delete the gameSession
        restGameSessionMockMvc
            .perform(delete(ENTITY_API_URL_ID, gameSession.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GameSession> gameSessionList = gameSessionRepository.findAll();
        assertThat(gameSessionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
