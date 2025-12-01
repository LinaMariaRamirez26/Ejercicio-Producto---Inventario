import { TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { ProductDetailComponent } from './product-detail.component';
import { ProductsService } from '../../api/productos.service';
import { InventoryService } from '../../api/inventario.service';
import { ActivatedRoute } from '@angular/router';

describe('ProductDetailComponent', () => {
  const product = { id: 1, nombre: 'Televisor', precio: 265, stock: 5 };
  const stock = { productId: 1, stock: 5 };

  function setup(
    services: {
      products?: Partial<ProductsService>;
      inventory?: Partial<InventoryService>;
    } = {}
  ) {
    TestBed.configureTestingModule({
      imports: [ProductDetailComponent],
      providers: [
        { provide: ProductsService, useValue: services.products ?? { getById: () => of(product) } },
        { provide: InventoryService, useValue: services.inventory ?? { getStock: () => of(stock), compra: vi.fn(() => of({ productId: 1, cantidad: 4 })) } },
        { provide: ActivatedRoute, useValue: { snapshot: { paramMap: new Map([['id', '1']]) } } },
      ],
    });
    const fixture = TestBed.createComponent(ProductDetailComponent);
    fixture.detectChanges();
    return fixture;
  }

  it('muestra datos del producto y stock', async () => {
    const fixture = setup();
    await fixture.whenStable();
    fixture.detectChanges();
    const el: HTMLElement = fixture.nativeElement;
    expect(el.querySelector('.detail-header .title')?.textContent).toContain('Televisor');
    expect(el.querySelector('.badge')?.textContent).toContain('Cantidad Disponible');
    expect(el.querySelector('.badge')?.textContent).toContain('5');
  });

  it('compra exitosa actualiza stock y muestra éxito', async () => {
    const compra = vi.fn(() => of({ productId: 1, cantidad: 4 }));
    const fixture = setup({ inventory: { getStock: () => of(stock), compra } });
    await fixture.whenStable();
    fixture.detectChanges();
    const el: HTMLElement = fixture.nativeElement;
    const btn = el.querySelector('.btn.primary') as HTMLButtonElement;
    btn.click();
    await fixture.whenStable();
    fixture.detectChanges();
    expect(el.querySelector('.badge')?.textContent).toContain('Cantidad Disponible');
    expect(el.querySelector('.badge')?.textContent).toContain('4');
    expect(el.querySelector('.alert.success')?.textContent).toContain('Compra realizada');
    expect(compra).toHaveBeenCalled();
  });

  it('bloquea compra si cantidad es menor a 1', async () => {
    const compra = vi.fn(() => of({ productId: 1, cantidad: 4 }));
    const fixture = setup({ inventory: { getStock: () => of(stock), compra } });
    await fixture.whenStable();
    fixture.detectChanges();
    const el: HTMLElement = fixture.nativeElement;
    const cmp: ProductDetailComponent = fixture.componentInstance;
    cmp.cantidad.set(0);
    fixture.detectChanges();
    const btn = el.querySelector('.btn.primary') as HTMLButtonElement;
    expect(btn.disabled).toBe(true);
    expect(compra).not.toHaveBeenCalled();
  });

  it('muestra error del backend al comprar', async () => {
    const compra = vi.fn(() => throwError(() => ({ error: { errors: [{ detail: 'Stock insuficiente' }] } })));
    const fixture = setup({ inventory: { getStock: () => of(stock), compra } });
    await fixture.whenStable();
    fixture.detectChanges();
    const el: HTMLElement = fixture.nativeElement;
    const btn = el.querySelector('.btn.primary') as HTMLButtonElement;
    btn.click();
    await fixture.whenStable();
    fixture.detectChanges();
    expect(el.querySelector('.alert')?.textContent).toContain('Stock insuficiente');
  });
});
