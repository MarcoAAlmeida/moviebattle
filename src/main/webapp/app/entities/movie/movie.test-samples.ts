import { IMovie, NewMovie } from './movie.model';

export const sampleWithRequiredData: IMovie = {
  id: 71608,
};

export const sampleWithPartialData: IMovie = {
  id: 92662,
  imdbRating: 64806,
  imdbVotes: 60543,
};

export const sampleWithFullData: IMovie = {
  id: 7802,
  imdbId: 'user-facing Licensed',
  title: 'Account',
  imdbRating: 98957,
  imdbVotes: 71887,
  score: 57442,
};

export const sampleWithNewData: NewMovie = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
