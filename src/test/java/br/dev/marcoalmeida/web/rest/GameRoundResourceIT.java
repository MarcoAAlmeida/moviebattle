package br.dev.marcoalmeida.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.dev.marcoalmeida.IntegrationTest;
import br.dev.marcoalmeida.domain.GameRound;
import br.dev.marcoalmeida.domain.enumeration.Choice;
import br.dev.marcoalmeida.repository.GameRoundRepository;
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
 * Integration tests for the {@link GameRoundResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GameRoundResourceIT {

    private static final String DEFAULT_GAME_SESSION_ID = "AAAAAAAAAA";
    private static final String UPDATED_GAME_SESSION_ID = "BBBBBBBBBB";

    private static final String DEFAULT_RIGHT_IMDB_ID = "AAAAAAAAAA";
    private static final String UPDATED_RIGHT_IMDB_ID = "BBBBBBBBBB";

    private static final String DEFAULT_LEFT_IMDB_ID = "AAAAAAAAAA";
    private static final String UPDATED_LEFT_IMDB_ID = "BBBBBBBBBB";

    private static final Choice DEFAULT_USER_CHOICE = Choice.RIGHT;
    private static final Choice UPDATED_USER_CHOICE = Choice.LEFT;

    private static final Boolean DEFAULT_CORRECT = false;
    private static final Boolean UPDATED_CORRECT = true;

    private static final String ENTITY_API_URL = "/api/game-rounds";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GameRoundRepository gameRoundRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGameRoundMockMvc;

    private GameRound gameRound;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GameRound createEntity(EntityManager em) {
        GameRound gameRound = new GameRound()
            .gameSessionId(DEFAULT_GAME_SESSION_ID)
            .rightImdbId(DEFAULT_RIGHT_IMDB_ID)
            .leftImdbId(DEFAULT_LEFT_IMDB_ID)
            .userChoice(DEFAULT_USER_CHOICE)
            .correct(DEFAULT_CORRECT);
        return gameRound;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GameRound createUpdatedEntity(EntityManager em) {
        GameRound gameRound = new GameRound()
            .gameSessionId(UPDATED_GAME_SESSION_ID)
            .rightImdbId(UPDATED_RIGHT_IMDB_ID)
            .leftImdbId(UPDATED_LEFT_IMDB_ID)
            .userChoice(UPDATED_USER_CHOICE)
            .correct(UPDATED_CORRECT);
        return gameRound;
    }

    @BeforeEach
    public void initTest() {
        gameRound = createEntity(em);
    }

    @Test
    @Transactional
    void createGameRound() throws Exception {
        int databaseSizeBeforeCreate = gameRoundRepository.findAll().size();
        // Create the GameRound
        restGameRoundMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameRound))
            )
            .andExpect(status().isCreated());

        // Validate the GameRound in the database
        List<GameRound> gameRoundList = gameRoundRepository.findAll();
        assertThat(gameRoundList).hasSize(databaseSizeBeforeCreate + 1);
        GameRound testGameRound = gameRoundList.get(gameRoundList.size() - 1);
        assertThat(testGameRound.getGameSessionId()).isEqualTo(DEFAULT_GAME_SESSION_ID);
        assertThat(testGameRound.getRightImdbId()).isEqualTo(DEFAULT_RIGHT_IMDB_ID);
        assertThat(testGameRound.getLeftImdbId()).isEqualTo(DEFAULT_LEFT_IMDB_ID);
        assertThat(testGameRound.getUserChoice()).isEqualTo(DEFAULT_USER_CHOICE);
        assertThat(testGameRound.getCorrect()).isEqualTo(DEFAULT_CORRECT);
    }

    @Test
    @Transactional
    void createGameRoundWithExistingId() throws Exception {
        // Create the GameRound with an existing ID
        gameRound.setId(1L);

        int databaseSizeBeforeCreate = gameRoundRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGameRoundMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameRound))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameRound in the database
        List<GameRound> gameRoundList = gameRoundRepository.findAll();
        assertThat(gameRoundList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGameRounds() throws Exception {
        // Initialize the database
        gameRoundRepository.saveAndFlush(gameRound);

        // Get all the gameRoundList
        restGameRoundMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gameRound.getId().intValue())))
            .andExpect(jsonPath("$.[*].gameSessionId").value(hasItem(DEFAULT_GAME_SESSION_ID)))
            .andExpect(jsonPath("$.[*].rightImdbId").value(hasItem(DEFAULT_RIGHT_IMDB_ID)))
            .andExpect(jsonPath("$.[*].leftImdbId").value(hasItem(DEFAULT_LEFT_IMDB_ID)))
            .andExpect(jsonPath("$.[*].userChoice").value(hasItem(DEFAULT_USER_CHOICE.toString())))
            .andExpect(jsonPath("$.[*].correct").value(hasItem(DEFAULT_CORRECT.booleanValue())));
    }

    @Test
    @Transactional
    void getGameRound() throws Exception {
        // Initialize the database
        gameRoundRepository.saveAndFlush(gameRound);

        // Get the gameRound
        restGameRoundMockMvc
            .perform(get(ENTITY_API_URL_ID, gameRound.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gameRound.getId().intValue()))
            .andExpect(jsonPath("$.gameSessionId").value(DEFAULT_GAME_SESSION_ID))
            .andExpect(jsonPath("$.rightImdbId").value(DEFAULT_RIGHT_IMDB_ID))
            .andExpect(jsonPath("$.leftImdbId").value(DEFAULT_LEFT_IMDB_ID))
            .andExpect(jsonPath("$.userChoice").value(DEFAULT_USER_CHOICE.toString()))
            .andExpect(jsonPath("$.correct").value(DEFAULT_CORRECT.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingGameRound() throws Exception {
        // Get the gameRound
        restGameRoundMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGameRound() throws Exception {
        // Initialize the database
        gameRoundRepository.saveAndFlush(gameRound);

        int databaseSizeBeforeUpdate = gameRoundRepository.findAll().size();

        // Update the gameRound
        GameRound updatedGameRound = gameRoundRepository.findById(gameRound.getId()).get();
        // Disconnect from session so that the updates on updatedGameRound are not directly saved in db
        em.detach(updatedGameRound);
        updatedGameRound
            .gameSessionId(UPDATED_GAME_SESSION_ID)
            .rightImdbId(UPDATED_RIGHT_IMDB_ID)
            .leftImdbId(UPDATED_LEFT_IMDB_ID)
            .userChoice(UPDATED_USER_CHOICE)
            .correct(UPDATED_CORRECT);

        restGameRoundMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGameRound.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGameRound))
            )
            .andExpect(status().isOk());

        // Validate the GameRound in the database
        List<GameRound> gameRoundList = gameRoundRepository.findAll();
        assertThat(gameRoundList).hasSize(databaseSizeBeforeUpdate);
        GameRound testGameRound = gameRoundList.get(gameRoundList.size() - 1);
        assertThat(testGameRound.getGameSessionId()).isEqualTo(UPDATED_GAME_SESSION_ID);
        assertThat(testGameRound.getRightImdbId()).isEqualTo(UPDATED_RIGHT_IMDB_ID);
        assertThat(testGameRound.getLeftImdbId()).isEqualTo(UPDATED_LEFT_IMDB_ID);
        assertThat(testGameRound.getUserChoice()).isEqualTo(UPDATED_USER_CHOICE);
        assertThat(testGameRound.getCorrect()).isEqualTo(UPDATED_CORRECT);
    }

    @Test
    @Transactional
    void putNonExistingGameRound() throws Exception {
        int databaseSizeBeforeUpdate = gameRoundRepository.findAll().size();
        gameRound.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGameRoundMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gameRound.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameRound))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameRound in the database
        List<GameRound> gameRoundList = gameRoundRepository.findAll();
        assertThat(gameRoundList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGameRound() throws Exception {
        int databaseSizeBeforeUpdate = gameRoundRepository.findAll().size();
        gameRound.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameRoundMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameRound))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameRound in the database
        List<GameRound> gameRoundList = gameRoundRepository.findAll();
        assertThat(gameRoundList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGameRound() throws Exception {
        int databaseSizeBeforeUpdate = gameRoundRepository.findAll().size();
        gameRound.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameRoundMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameRound))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GameRound in the database
        List<GameRound> gameRoundList = gameRoundRepository.findAll();
        assertThat(gameRoundList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGameRoundWithPatch() throws Exception {
        // Initialize the database
        gameRoundRepository.saveAndFlush(gameRound);

        int databaseSizeBeforeUpdate = gameRoundRepository.findAll().size();

        // Update the gameRound using partial update
        GameRound partialUpdatedGameRound = new GameRound();
        partialUpdatedGameRound.setId(gameRound.getId());

        partialUpdatedGameRound.rightImdbId(UPDATED_RIGHT_IMDB_ID).correct(UPDATED_CORRECT);

        restGameRoundMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGameRound.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGameRound))
            )
            .andExpect(status().isOk());

        // Validate the GameRound in the database
        List<GameRound> gameRoundList = gameRoundRepository.findAll();
        assertThat(gameRoundList).hasSize(databaseSizeBeforeUpdate);
        GameRound testGameRound = gameRoundList.get(gameRoundList.size() - 1);
        assertThat(testGameRound.getGameSessionId()).isEqualTo(DEFAULT_GAME_SESSION_ID);
        assertThat(testGameRound.getRightImdbId()).isEqualTo(UPDATED_RIGHT_IMDB_ID);
        assertThat(testGameRound.getLeftImdbId()).isEqualTo(DEFAULT_LEFT_IMDB_ID);
        assertThat(testGameRound.getUserChoice()).isEqualTo(DEFAULT_USER_CHOICE);
        assertThat(testGameRound.getCorrect()).isEqualTo(UPDATED_CORRECT);
    }

    @Test
    @Transactional
    void fullUpdateGameRoundWithPatch() throws Exception {
        // Initialize the database
        gameRoundRepository.saveAndFlush(gameRound);

        int databaseSizeBeforeUpdate = gameRoundRepository.findAll().size();

        // Update the gameRound using partial update
        GameRound partialUpdatedGameRound = new GameRound();
        partialUpdatedGameRound.setId(gameRound.getId());

        partialUpdatedGameRound
            .gameSessionId(UPDATED_GAME_SESSION_ID)
            .rightImdbId(UPDATED_RIGHT_IMDB_ID)
            .leftImdbId(UPDATED_LEFT_IMDB_ID)
            .userChoice(UPDATED_USER_CHOICE)
            .correct(UPDATED_CORRECT);

        restGameRoundMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGameRound.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGameRound))
            )
            .andExpect(status().isOk());

        // Validate the GameRound in the database
        List<GameRound> gameRoundList = gameRoundRepository.findAll();
        assertThat(gameRoundList).hasSize(databaseSizeBeforeUpdate);
        GameRound testGameRound = gameRoundList.get(gameRoundList.size() - 1);
        assertThat(testGameRound.getGameSessionId()).isEqualTo(UPDATED_GAME_SESSION_ID);
        assertThat(testGameRound.getRightImdbId()).isEqualTo(UPDATED_RIGHT_IMDB_ID);
        assertThat(testGameRound.getLeftImdbId()).isEqualTo(UPDATED_LEFT_IMDB_ID);
        assertThat(testGameRound.getUserChoice()).isEqualTo(UPDATED_USER_CHOICE);
        assertThat(testGameRound.getCorrect()).isEqualTo(UPDATED_CORRECT);
    }

    @Test
    @Transactional
    void patchNonExistingGameRound() throws Exception {
        int databaseSizeBeforeUpdate = gameRoundRepository.findAll().size();
        gameRound.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGameRoundMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gameRound.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gameRound))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameRound in the database
        List<GameRound> gameRoundList = gameRoundRepository.findAll();
        assertThat(gameRoundList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGameRound() throws Exception {
        int databaseSizeBeforeUpdate = gameRoundRepository.findAll().size();
        gameRound.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameRoundMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gameRound))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameRound in the database
        List<GameRound> gameRoundList = gameRoundRepository.findAll();
        assertThat(gameRoundList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGameRound() throws Exception {
        int databaseSizeBeforeUpdate = gameRoundRepository.findAll().size();
        gameRound.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameRoundMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gameRound))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GameRound in the database
        List<GameRound> gameRoundList = gameRoundRepository.findAll();
        assertThat(gameRoundList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGameRound() throws Exception {
        // Initialize the database
        gameRoundRepository.saveAndFlush(gameRound);

        int databaseSizeBeforeDelete = gameRoundRepository.findAll().size();

        // Delete the gameRound
        restGameRoundMockMvc
            .perform(delete(ENTITY_API_URL_ID, gameRound.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GameRound> gameRoundList = gameRoundRepository.findAll();
        assertThat(gameRoundList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
