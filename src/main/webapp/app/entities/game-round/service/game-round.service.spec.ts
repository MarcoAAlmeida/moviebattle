import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IGameRound } from '../game-round.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../game-round.test-samples';

import { GameRoundService } from './game-round.service';

const requireRestSample: IGameRound = {
  ...sampleWithRequiredData,
};

describe('GameRound Service', () => {
  let service: GameRoundService;
  let httpMock: HttpTestingController;
  let expectedResult: IGameRound | IGameRound[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(GameRoundService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a GameRound', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const gameRound = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(gameRound).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a GameRound', () => {
      const gameRound = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(gameRound).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a GameRound', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of GameRound', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a GameRound', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addGameRoundToCollectionIfMissing', () => {
      it('should add a GameRound to an empty array', () => {
        const gameRound: IGameRound = sampleWithRequiredData;
        expectedResult = service.addGameRoundToCollectionIfMissing([], gameRound);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(gameRound);
      });

      it('should not add a GameRound to an array that contains it', () => {
        const gameRound: IGameRound = sampleWithRequiredData;
        const gameRoundCollection: IGameRound[] = [
          {
            ...gameRound,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addGameRoundToCollectionIfMissing(gameRoundCollection, gameRound);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a GameRound to an array that doesn't contain it", () => {
        const gameRound: IGameRound = sampleWithRequiredData;
        const gameRoundCollection: IGameRound[] = [sampleWithPartialData];
        expectedResult = service.addGameRoundToCollectionIfMissing(gameRoundCollection, gameRound);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(gameRound);
      });

      it('should add only unique GameRound to an array', () => {
        const gameRoundArray: IGameRound[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const gameRoundCollection: IGameRound[] = [sampleWithRequiredData];
        expectedResult = service.addGameRoundToCollectionIfMissing(gameRoundCollection, ...gameRoundArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const gameRound: IGameRound = sampleWithRequiredData;
        const gameRound2: IGameRound = sampleWithPartialData;
        expectedResult = service.addGameRoundToCollectionIfMissing([], gameRound, gameRound2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(gameRound);
        expect(expectedResult).toContain(gameRound2);
      });

      it('should accept null and undefined values', () => {
        const gameRound: IGameRound = sampleWithRequiredData;
        expectedResult = service.addGameRoundToCollectionIfMissing([], null, gameRound, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(gameRound);
      });

      it('should return initial array if no GameRound is added', () => {
        const gameRoundCollection: IGameRound[] = [sampleWithRequiredData];
        expectedResult = service.addGameRoundToCollectionIfMissing(gameRoundCollection, undefined, null);
        expect(expectedResult).toEqual(gameRoundCollection);
      });
    });

    describe('compareGameRound', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareGameRound(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareGameRound(entity1, entity2);
        const compareResult2 = service.compareGameRound(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareGameRound(entity1, entity2);
        const compareResult2 = service.compareGameRound(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareGameRound(entity1, entity2);
        const compareResult2 = service.compareGameRound(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
