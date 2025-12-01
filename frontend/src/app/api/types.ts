export interface ApiResource<T> {
  id: string;
  type: string;
  attributes: T;
}

export interface ApiResponse<T> {
  data: ApiResource<T>;
}

export interface ApiListResponse<T> {
  data: ApiResource<T>[];
}

export interface ProductResponse {
  id: number;
  nombre: string;
  precio: number;
  stock: number;
}

export interface StockResponse {
  productId: number;
  stock: number;
}

export interface CreateInventoryRequest {
  productId: number;
  cantidad: number;
}

export interface InventoryAttributes {
  productId: number;
  cantidad: number;
}

