import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GameRoundDetailComponent } from './game-round-detail.component';

describe('GameRound Management Detail Component', () => {
  let comp: GameRoundDetailComponent;
  let fixture: ComponentFixture<GameRoundDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GameRoundDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ gameRound: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(GameRoundDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(GameRoundDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load gameRound on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.gameRound).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
