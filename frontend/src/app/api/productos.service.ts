import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { ApiListResponse, ApiResponse, ApiResource, ProductResponse } from './types';

const PRODUCTS_BASE = 'http://localhost:8081/productos';

@Injectable({ providedIn: 'root' })
export class ProductsService {
  private http = inject(HttpClient);

  list(): Observable<ProductResponse[]> {
    return this.http
      .get<ApiListResponse<ProductResponse>>(PRODUCTS_BASE)
      .pipe(map((resp) => resp.data.map((r: ApiResource<ProductResponse>) => ({ ...r.attributes, id: Number(r.id) }))));
  }

  getById(id: number): Observable<ProductResponse> {
    return this.http
      .get<ApiResponse<ProductResponse>>(`${PRODUCTS_BASE}/${id}`)
      .pipe(map((resp) => ({ ...resp.data.attributes, id: Number(resp.data.id) })));
  }
}
