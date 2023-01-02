import { IGameSession, NewGameSession } from './game-session.model';

export const sampleWithRequiredData: IGameSession = {
  id: '6641a40f-464f-4952-a0e2-aabaa6b7e6c1',
};

export const sampleWithPartialData: IGameSession = {
  id: 'e2d53e61-7ccb-43be-af41-1acc5e06828c',
  userId: 61207,
};

export const sampleWithFullData: IGameSession = {
  id: '151642f2-2b27-4655-9f69-2187bfd31aad',
  userId: 72114,
};

export const sampleWithNewData: NewGameSession = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
