import { HttpInterceptorFn, HttpRequest } from '@angular/common/http';

const API_KEY = 'app-secret';

export const apiInterceptor: HttpInterceptorFn = (req, next) => {
  let request: HttpRequest<any> = req;
  request = req.clone({ setHeaders: { 'X-API-Key': API_KEY } });
  return next(request);
};

