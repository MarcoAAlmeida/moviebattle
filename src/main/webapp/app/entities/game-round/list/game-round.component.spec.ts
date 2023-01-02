import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { GameRoundService } from '../service/game-round.service';

import { GameRoundComponent } from './game-round.component';

describe('GameRound Management Component', () => {
  let comp: GameRoundComponent;
  let fixture: ComponentFixture<GameRoundComponent>;
  let service: GameRoundService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'game-round', component: GameRoundComponent }]), HttpClientTestingModule],
      declarations: [GameRoundComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(GameRoundComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GameRoundComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(GameRoundService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.gameRounds?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to gameRoundService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getGameRoundIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getGameRoundIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
