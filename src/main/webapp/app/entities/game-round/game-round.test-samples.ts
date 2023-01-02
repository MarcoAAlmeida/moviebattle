import { Choice } from 'app/entities/enumerations/choice.model';

import { IGameRound, NewGameRound } from './game-round.model';

export const sampleWithRequiredData: IGameRound = {
  id: 2588,
};

export const sampleWithPartialData: IGameRound = {
  id: 67892,
  rightImdbId: 'facilitate hacking',
  userChoice: Choice['RIGHT'],
  correct: false,
};

export const sampleWithFullData: IGameRound = {
  id: 64232,
  gameSessionId: 'payment back Refined',
  rightImdbId: 'withdrawal',
  leftImdbId: 'bypassing morph',
  userChoice: Choice['LEFT'],
  correct: true,
};

export const sampleWithNewData: NewGameRound = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
