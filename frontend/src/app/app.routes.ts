import { Routes } from '@angular/router';
import { ProductsListComponent } from './features/component-productos/products-list.component';
import { ProductDetailComponent } from './features/component-detalle-producto/product-detail.component';

export const routes: Routes = [
  { path: '', redirectTo: 'productos', pathMatch: 'full' },
  { path: 'productos', component: ProductsListComponent },
  { path: 'productos/:id', component: ProductDetailComponent },
  { path: '**', redirectTo: 'productos' },
];
