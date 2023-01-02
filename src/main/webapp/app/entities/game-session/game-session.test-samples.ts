import { IGameSession, NewGameSession } from './game-session.model';

export const sampleWithRequiredData: IGameSession = {
  id: 39322,
};

export const sampleWithPartialData: IGameSession = {
  id: 12471,
};

export const sampleWithFullData: IGameSession = {
  id: 65818,
  userId: 'Tasty',
  finished: true,
};

export const sampleWithNewData: NewGameSession = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
