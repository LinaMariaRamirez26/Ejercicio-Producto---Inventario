import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { ApiResponse, InventoryAttributes, StockResponse } from './types';

const INVENTORY_BASE = 'http://localhost:8083/inventario';

@Injectable({ providedIn: 'root' })
export class InventoryService {
  private http = inject(HttpClient);

  getStock(productId: number): Observable<StockResponse> {
    return this.http
      .get<ApiResponse<StockResponse>>(`${INVENTORY_BASE}/${productId}/stock`)
      .pipe(map((resp) => resp.data.attributes));
  }

  compra(productId: number, cantidad: number): Observable<InventoryAttributes> {
    return this.http
      .post<ApiResponse<InventoryAttributes>>(`${INVENTORY_BASE}/compra`, { productId, cantidad })
      .pipe(map((resp) => resp.data.attributes));
  }
}
