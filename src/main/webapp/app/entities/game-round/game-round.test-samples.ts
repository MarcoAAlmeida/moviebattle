import { Choice } from 'app/entities/enumerations/choice.model';

import { IGameRound, NewGameRound } from './game-round.model';

export const sampleWithRequiredData: IGameRound = {
  id: 2588,
};

export const sampleWithPartialData: IGameRound = {
  id: 1144,
  correct: true,
};

export const sampleWithFullData: IGameRound = {
  id: 65816,
  userChoice: Choice['NONE'],
  correct: false,
};

export const sampleWithNewData: NewGameRound = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
