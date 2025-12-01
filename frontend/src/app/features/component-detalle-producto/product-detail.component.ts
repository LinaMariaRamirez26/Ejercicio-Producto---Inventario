import { Component, inject, signal, computed } from '@angular/core';
import { AsyncPipe, NgIf } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ProductsService } from '../../api/productos.service';
import { InventoryService } from '../../api/inventario.service';
import { ProductResponse, StockResponse } from '../../api/types';

@Component({
  selector: 'app-product-detail',
  imports: [NgIf, AsyncPipe, RouterLink],
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.scss'],
})
export class ProductDetailComponent {
  private readonly route = inject(ActivatedRoute);
  private readonly products = inject(ProductsService);
  private readonly inventory = inject(InventoryService);

  readonly product = signal<ProductResponse | null>(null);
  readonly stock = signal<StockResponse | null>(null);
  readonly cantidad = signal(1);
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  readonly success = signal<string | null>(null);
  readonly disponible = computed(() => (this.stock()?.stock ?? this.product()?.stock ?? 0));

  constructor() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.loading.set(true);
    this.products.getById(id).subscribe({
      next: (p) => {
        this.product.set(p);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Error consultando producto');
        this.loading.set(false);
      },
    });
    this.inventory.getStock(id).subscribe({
      next: (s) => this.stock.set(s),
      error: () => {},
    });
  }

  onCantidad(e: Event) {
    const v = Number((e.target as HTMLInputElement).value);
    const max = this.disponible();
    this.cantidad.set(Math.max(1, Math.min(v, max)));
    this.success.set(null);
    this.error.set(null);
    (e.target as HTMLInputElement).value = String(this.cantidad());
  }

  comprar() {
    const p = this.product();
    if (!p) return;
    if (this.cantidad() < 1 || this.cantidad() > this.disponible()) {
      this.error.set('Stock insuficiente');
      return;
    }
    this.loading.set(true);
    this.error.set(null);
    this.success.set(null);
    this.inventory.compra(p.id, this.cantidad()).subscribe({
      next: (res) => {
        this.stock.set({ productId: res.productId, stock: res.cantidad });
        const disponible = Math.max(0, res.cantidad);
        this.cantidad.set(Math.max(1, Math.min(this.cantidad(), disponible)));
        this.error.set(null);
        this.success.set(`Compra realizada. Stock restante: ${res.cantidad}`);
        this.loading.set(false);
      },
      error: (err) => {
        const detail = err?.error?.errors?.[0]?.detail ?? 'Error procesando compra';
        this.error.set(detail);
        this.loading.set(false);
      },
    });
  }
}
