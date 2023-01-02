import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { MovieFormService, MovieFormGroup } from './movie-form.service';
import { IMovie } from '../movie.model';
import { MovieService } from '../service/movie.service';

@Component({
  selector: 'jhi-movie-update',
  templateUrl: './movie-update.component.html',
})
export class MovieUpdateComponent implements OnInit {
  isSaving = false;
  movie: IMovie | null = null;

  editForm: MovieFormGroup = this.movieFormService.createMovieFormGroup();

  constructor(
    protected movieService: MovieService,
    protected movieFormService: MovieFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ movie }) => {
      this.movie = movie;
      if (movie) {
        this.updateForm(movie);
      }
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
  }
}
