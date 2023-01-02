import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { GameSessionService } from '../service/game-session.service';

import { GameSessionComponent } from './game-session.component';

describe('GameSession Management Component', () => {
  let comp: GameSessionComponent;
  let fixture: ComponentFixture<GameSessionComponent>;
  let service: GameSessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'game-session', component: GameSessionComponent }]), HttpClientTestingModule],
      declarations: [GameSessionComponent],
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
      .overrideTemplate(GameSessionComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GameSessionComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(GameSessionService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 'ABC' }],
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
    expect(comp.gameSessions?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });

  describe('trackId', () => {
    it('Should forward to gameSessionService', () => {
      const entity = { id: 'ABC' };
      jest.spyOn(service, 'getGameSessionIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getGameSessionIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
