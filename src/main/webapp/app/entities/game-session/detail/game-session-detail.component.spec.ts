import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GameSessionDetailComponent } from './game-session-detail.component';

describe('GameSession Management Detail Component', () => {
  let comp: GameSessionDetailComponent;
  let fixture: ComponentFixture<GameSessionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GameSessionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ gameSession: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(GameSessionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(GameSessionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load gameSession on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.gameSession).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
