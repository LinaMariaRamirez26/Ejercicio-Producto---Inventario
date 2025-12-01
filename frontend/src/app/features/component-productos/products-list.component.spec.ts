import { TestBed } from '@angular/core/testing';
import { of, throwError, Subject } from 'rxjs';
import { ProductsListComponent } from './products-list.component';
import { RouterTestingModule } from '@angular/router/testing';
import { ProductsService } from '../../api/productos.service';

describe('ProductsListComponent', () => {
  const mockProducts = [
    { id: 1, nombre: 'Televisor', precio: 265, stock: 580 },
    { id: 2, nombre: 'Nevera', precio: 356.89, stock: 10 },
  ];

  function setup(serviceMock: Partial<ProductsService>) {
    TestBed.configureTestingModule({
      imports: [ProductsListComponent, RouterTestingModule],
      providers: [
        { provide: ProductsService, useValue: serviceMock },
      ],
    });
    const fixture = TestBed.createComponent(ProductsListComponent);
    fixture.detectChanges();
    return fixture;
  }

  it('renderiza listado y paginación', async () => {
    const fixture = setup({ list: () => of(mockProducts) } as ProductsService);
    await fixture.whenStable();
    fixture.detectChanges();
    const el: HTMLElement = fixture.nativeElement;
    const items = el.querySelectorAll('ul.grid > li.card');
    expect(items.length).toBe(2);
    expect(el.querySelector('.pagination')).toBeTruthy();
  });

  it('muestra estado de carga', () => {
    const subject = new Subject<typeof mockProducts>();
    const fixture = setup({ list: () => subject.asObservable() } as unknown as ProductsService);
    const el: HTMLElement = fixture.nativeElement;
    expect(el.querySelector('.loading')).toBeTruthy();
  });

  it('maneja error y reintento', async () => {
    const listSpy = vi.fn()
      .mockReturnValueOnce(throwError(() => new Error('fallo')))
      .mockReturnValueOnce(of(mockProducts));
    const fixture = setup({ list: listSpy } as unknown as ProductsService);
    await fixture.whenStable();
    fixture.detectChanges();
    let el: HTMLElement = fixture.nativeElement;
    expect(el.querySelector('.alert')).toBeTruthy();
    const retry = el.querySelector('.alert .btn') as HTMLButtonElement;
    retry.click();
    await fixture.whenStable();
    fixture.detectChanges();
    el = fixture.nativeElement;
    expect(el.querySelector('.alert')).toBeFalsy();
    const items = el.querySelectorAll('ul.grid > li.card');
    expect(items.length).toBe(2);
    expect(listSpy).toHaveBeenCalledTimes(2);
  });
});
