import { Component, signal, computed, inject } from '@angular/core';
import { AsyncPipe, NgForOf, NgIf } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ProductsService } from '../../api/productos.service';
import { ProductResponse } from '../../api/types';

@Component({
  selector: 'app-products-list',
  imports: [NgIf, NgForOf, AsyncPipe, RouterLink],
  templateUrl: './products-list.component.html',
  styleUrls: ['./products-list.component.scss'],
})
export class ProductsListComponent {
  private readonly service = inject(ProductsService);
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  readonly items = signal<ProductResponse[]>([]);
  readonly page = signal(1);
  readonly pageSize = signal(9);

  readonly totalPages = computed(() => Math.max(1, Math.ceil(this.items().length / this.pageSize())));
  readonly pageItems = computed(() => {
    const start = (this.page() - 1) * this.pageSize();
    return this.items().slice(start, start + this.pageSize());
  });

  constructor() {
    this.fetch();
  }

  fetch() {
    this.loading.set(true);
    this.error.set(null);
    this.service.list().subscribe({
      next: (list: ProductResponse[]) => {
        this.items.set(list);
        this.page.set(1);
        this.loading.set(false);
      },
      error: (e: any) => {
        this.error.set('Error cargando productos');
        this.loading.set(false);
      },
    });
  }

  next() {
    if (this.page() < this.totalPages()) this.page.set(this.page() + 1);
  }

  prev() {
    if (this.page() > 1) this.page.set(this.page() - 1);
  }
}
