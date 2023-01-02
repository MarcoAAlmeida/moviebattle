import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { MovieFormService, MovieFormGroup } from './movie-form.service';
import { IMovie } from '../movie.model';
import { MovieService } from '../service/movie.service';
import { IGameRound } from 'app/entities/game-round/game-round.model';
import { GameRoundService } from 'app/entities/game-round/service/game-round.service';

@Component({
  selector: 'jhi-movie-update',
  templateUrl: './movie-update.component.html',
})
export class MovieUpdateComponent implements OnInit {
  isSaving = false;
  movie: IMovie | null = null;

  gameRoundsSharedCollection: IGameRound[] = [];

  editForm: MovieFormGroup = this.movieFormService.createMovieFormGroup();

  constructor(
    protected movieService: MovieService,
    protected movieFormService: MovieFormService,
    protected gameRoundService: GameRoundService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareGameRound = (o1: IGameRound | null, o2: IGameRound | null): boolean => this.gameRoundService.compareGameRound(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ movie }) => {
      this.movie = movie;
      if (movie) {
        this.updateForm(movie);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const movie = this.movieFormService.getMovie(this.editForm);
    if (movie.id !== null) {
      this.subscribeToSaveResponse(this.movieService.update(movie));
    } else {
      this.subscribeToSaveResponse(this.movieService.create(movie));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMovie>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(movie: IMovie): void {
    this.movie = movie;
    this.movieFormService.resetForm(this.editForm, movie);

    this.gameRoundsSharedCollection = this.gameRoundService.addGameRoundToCollectionIfMissing<IGameRound>(
      this.gameRoundsSharedCollection,
      ...(movie.rights ?? []),
      ...(movie.lefts ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.gameRoundService
      .query()
      .pipe(map((res: HttpResponse<IGameRound[]>) => res.body ?? []))
      .pipe(
        map((gameRounds: IGameRound[]) =>
          this.gameRoundService.addGameRoundToCollectionIfMissing<IGameRound>(
            gameRounds,
            ...(this.movie?.rights ?? []),
            ...(this.movie?.lefts ?? [])
          )
        )
      )
      .subscribe((gameRounds: IGameRound[]) => (this.gameRoundsSharedCollection = gameRounds));
  }
}
