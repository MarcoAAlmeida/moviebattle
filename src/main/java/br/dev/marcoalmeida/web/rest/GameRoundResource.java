package br.dev.marcoalmeida.web.rest;

import br.dev.marcoalmeida.domain.GameRound;
import br.dev.marcoalmeida.repository.GameRoundRepository;
import br.dev.marcoalmeida.service.GameRoundService;
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
 * REST controller for managing {@link br.dev.marcoalmeida.domain.GameRound}.
 */
@RestController
@RequestMapping("/api")
public class GameRoundResource {

    private final Logger log = LoggerFactory.getLogger(GameRoundResource.class);

    private static final String ENTITY_NAME = "gameRound";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GameRoundService gameRoundService;

    private final GameRoundRepository gameRoundRepository;

    public GameRoundResource(GameRoundService gameRoundService, GameRoundRepository gameRoundRepository) {
        this.gameRoundService = gameRoundService;
        this.gameRoundRepository = gameRoundRepository;
    }

    /**
     * {@code POST  /game-rounds} : Create a new gameRound.
     *
     * @param gameRound the gameRound to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gameRound, or with status {@code 400 (Bad Request)} if the gameRound has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/game-rounds")
    public ResponseEntity<GameRound> createGameRound(@RequestBody GameRound gameRound) throws URISyntaxException {
        log.debug("REST request to save GameRound : {}", gameRound);
        if (gameRound.getId() != null) {
            throw new BadRequestAlertException("A new gameRound cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GameRound result = gameRoundService.save(gameRound);
        return ResponseEntity
            .created(new URI("/api/game-rounds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /game-rounds/:id} : Updates an existing gameRound.
     *
     * @param id the id of the gameRound to save.
     * @param gameRound the gameRound to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gameRound,
     * or with status {@code 400 (Bad Request)} if the gameRound is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gameRound couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/game-rounds/{id}")
    public ResponseEntity<GameRound> updateGameRound(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GameRound gameRound
    ) throws URISyntaxException {
        log.debug("REST request to update GameRound : {}, {}", id, gameRound);
        if (gameRound.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gameRound.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gameRoundRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GameRound result = gameRoundService.update(gameRound);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gameRound.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /game-rounds/:id} : Partial updates given fields of an existing gameRound, field will ignore if it is null
     *
     * @param id the id of the gameRound to save.
     * @param gameRound the gameRound to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gameRound,
     * or with status {@code 400 (Bad Request)} if the gameRound is not valid,
     * or with status {@code 404 (Not Found)} if the gameRound is not found,
     * or with status {@code 500 (Internal Server Error)} if the gameRound couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/game-rounds/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GameRound> partialUpdateGameRound(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GameRound gameRound
    ) throws URISyntaxException {
        log.debug("REST request to partial update GameRound partially : {}, {}", id, gameRound);
        if (gameRound.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gameRound.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gameRoundRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GameRound> result = gameRoundService.partialUpdate(gameRound);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gameRound.getId().toString())
        );
    }

    /**
     * {@code GET  /game-rounds} : get all the gameRounds.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gameRounds in body.
     */
    @GetMapping("/game-rounds")
    public List<GameRound> getAllGameRounds() {
        log.debug("REST request to get all GameRounds");
        return gameRoundService.findAll();
    }

    /**
     * {@code GET  /game-rounds/:id} : get the "id" gameRound.
     *
     * @param id the id of the gameRound to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gameRound, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/game-rounds/{id}")
    public ResponseEntity<GameRound> getGameRound(@PathVariable Long id) {
        log.debug("REST request to get GameRound : {}", id);
        Optional<GameRound> gameRound = gameRoundService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gameRound);
    }

    /**
     * {@code DELETE  /game-rounds/:id} : delete the "id" gameRound.
     *
     * @param id the id of the gameRound to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/game-rounds/{id}")
    public ResponseEntity<Void> deleteGameRound(@PathVariable Long id) {
        log.debug("REST request to delete GameRound : {}", id);
        gameRoundService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
