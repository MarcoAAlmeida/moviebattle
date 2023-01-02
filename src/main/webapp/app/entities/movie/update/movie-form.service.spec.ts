import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../movie.test-samples';

import { MovieFormService } from './movie-form.service';

describe('Movie Form Service', () => {
  let service: MovieFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MovieFormService);
  });

  describe('Service methods', () => {
    describe('createMovieFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMovieFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            imdbId: expect.any(Object),
            title: expect.any(Object),
            imdbRating: expect.any(Object),
            imdbVotes: expect.any(Object),
            score: expect.any(Object),
          })
        );
      });

      it('passing IMovie should create a new form with FormGroup', () => {
        const formGroup = service.createMovieFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            imdbId: expect.any(Object),
            title: expect.any(Object),
            imdbRating: expect.any(Object),
            imdbVotes: expect.any(Object),
            score: expect.any(Object),
          })
        );
      });
    });

    describe('getMovie', () => {
      it('should return NewMovie for default Movie initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createMovieFormGroup(sampleWithNewData);

        const movie = service.getMovie(formGroup) as any;

        expect(movie).toMatchObject(sampleWithNewData);
      });

      it('should return NewMovie for empty Movie initial value', () => {
        const formGroup = service.createMovieFormGroup();

        const movie = service.getMovie(formGroup) as any;

        expect(movie).toMatchObject({});
      });

      it('should return IMovie', () => {
        const formGroup = service.createMovieFormGroup(sampleWithRequiredData);

        const movie = service.getMovie(formGroup) as any;

        expect(movie).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMovie should not enable id FormControl', () => {
        const formGroup = service.createMovieFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMovie should disable id FormControl', () => {
        const formGroup = service.createMovieFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
