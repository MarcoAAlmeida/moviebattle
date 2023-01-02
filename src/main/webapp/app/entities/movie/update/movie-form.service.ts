import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMovie, NewMovie } from '../movie.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMovie for edit and NewMovieFormGroupInput for create.
 */
type MovieFormGroupInput = IMovie | PartialWithRequiredKeyOf<NewMovie>;

type MovieFormDefaults = Pick<NewMovie, 'id'>;

type MovieFormGroupContent = {
  id: FormControl<IMovie['id'] | NewMovie['id']>;
  imdbId: FormControl<IMovie['imdbId']>;
  title: FormControl<IMovie['title']>;
  imdbRating: FormControl<IMovie['imdbRating']>;
  imdbVotes: FormControl<IMovie['imdbVotes']>;
  score: FormControl<IMovie['score']>;
};

export type MovieFormGroup = FormGroup<MovieFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MovieFormService {
  createMovieFormGroup(movie: MovieFormGroupInput = { id: null }): MovieFormGroup {
    const movieRawValue = {
      ...this.getFormDefaults(),
      ...movie,
    };
    return new FormGroup<MovieFormGroupContent>({
      id: new FormControl(
        { value: movieRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      imdbId: new FormControl(movieRawValue.imdbId),
      title: new FormControl(movieRawValue.title),
      imdbRating: new FormControl(movieRawValue.imdbRating),
      imdbVotes: new FormControl(movieRawValue.imdbVotes),
      score: new FormControl(movieRawValue.score),
    });
  }

  getMovie(form: MovieFormGroup): IMovie | NewMovie {
    return form.getRawValue() as IMovie | NewMovie;
  }

  resetForm(form: MovieFormGroup, movie: MovieFormGroupInput): void {
    const movieRawValue = { ...this.getFormDefaults(), ...movie };
    form.reset(
      {
        ...movieRawValue,
        id: { value: movieRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): MovieFormDefaults {
    return {
      id: null,
    };
  }
}
