package br.dev.marcoalmeida.web.rest;

import br.dev.marcoalmeida.domain.GameSession;
import br.dev.marcoalmeida.repository.GameSessionRepository;
import br.dev.marcoalmeida.service.GameSessionService;
import br.dev.marcoalmeida.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.dev.marcoalmeida.domain.GameSession}.
 */
@RestController
@RequestMapping("/api")
public class GameSessionResource {

    private final Logger log = LoggerFactory.getLogger(GameSessionResource.class);

    private static final String ENTITY_NAME = "gameSession";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GameSessionService gameSessionService;

    private final GameSessionRepository gameSessionRepository;

    public GameSessionResource(GameSessionService gameSessionService, GameSessionRepository gameSessionRepository) {
        this.gameSessionService = gameSessionService;
        this.gameSessionRepository = gameSessionRepository;
    }

    /**
     * {@code POST  /game-sessions} : Create a new gameSession.
     *
     * @param gameSession the gameSession to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gameSession, or with status {@code 400 (Bad Request)} if the gameSession has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/game-sessions")
    public ResponseEntity<GameSession> createGameSession(@RequestBody GameSession gameSession) throws URISyntaxException {
        log.debug("REST request to save GameSession : {}", gameSession);
        if (gameSession.getId() != null) {
            throw new BadRequestAlertException("A new gameSession cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GameSession result = gameSessionService.save(gameSession);
        return ResponseEntity
            .created(new URI("/api/game-sessions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /game-sessions/:id} : Updates an existing gameSession.
     *
     * @param id the id of the gameSession to save.
     * @param gameSession the gameSession to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gameSession,
     * or with status {@code 400 (Bad Request)} if the gameSession is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gameSession couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/game-sessions/{id}")
    public ResponseEntity<GameSession> updateGameSession(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody GameSession gameSession
    ) throws URISyntaxException {
        log.debug("REST request to update GameSession : {}, {}", id, gameSession);
        if (gameSession.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gameSession.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gameSessionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GameSession result = gameSessionService.update(gameSession);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gameSession.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /game-sessions/:id} : Partial updates given fields of an existing gameSession, field will ignore if it is null
     *
     * @param id the id of the gameSession to save.
     * @param gameSession the gameSession to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gameSession,
     * or with status {@code 400 (Bad Request)} if the gameSession is not valid,
     * or with status {@code 404 (Not Found)} if the gameSession is not found,
     * or with status {@code 500 (Internal Server Error)} if the gameSession couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/game-sessions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GameSession> partialUpdateGameSession(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody GameSession gameSession
    ) throws URISyntaxException {
        log.debug("REST request to partial update GameSession partially : {}, {}", id, gameSession);
        if (gameSession.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gameSession.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gameSessionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GameSession> result = gameSessionService.partialUpdate(gameSession);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gameSession.getId())
        );
    }

    /**
     * {@code GET  /game-sessions} : get all the gameSessions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gameSessions in body.
     */
    @GetMapping("/game-sessions")
    public List<GameSession> getAllGameSessions() {
        log.debug("REST request to get all GameSessions");
        return gameSessionService.findAll();
    }

    /**
     * {@code GET  /game-sessions/:id} : get the "id" gameSession.
     *
     * @param id the id of the gameSession to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gameSession, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/game-sessions/{id}")
    public ResponseEntity<GameSession> getGameSession(@PathVariable String id) {
        log.debug("REST request to get GameSession : {}", id);
        Optional<GameSession> gameSession = gameSessionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gameSession);
    }

    /**
     * {@code DELETE  /game-sessions/:id} : delete the "id" gameSession.
     *
     * @param id the id of the gameSession to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/game-sessions/{id}")
    public ResponseEntity<Void> deleteGameSession(@PathVariable String id) {
        log.debug("REST request to delete GameSession : {}", id);
        gameSessionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
