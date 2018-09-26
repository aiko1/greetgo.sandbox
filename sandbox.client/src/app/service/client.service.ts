import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {catchError} from 'rxjs/operators';
import {Observable, of} from 'rxjs';
import {ClientRecord} from "../../model/ClientRecord";
import {ClientDetail} from "../../model/ClientDetail";
import {Charm} from "../../model/Charm";
import {HttpService} from "../http.service";

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'}),
  params: null
};

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  // private url: string = 'http://localhost:3000';
  private url: string = 'http://localhost:1313/sandbox/api';

  constructor(private _http: HttpClient, private http: HttpService) {
  }

  getClientRecords(params): Observable<ClientRecord[]> {
    httpOptions.params = params;
    return this._http.get<ClientRecord[]>(this.url + '/client/list', httpOptions)
      .pipe(
        catchError(this.handleError('getClients', []))
      );

    // return this._http.get<ClientRecord[]>(this.url + '/clientRecord?_page=1&name=Игорь', httpOptions)
    //   .pipe(
    //     catchError(this.handleError('getClients', []))
    //   );
  }

  getAddedClient(): Observable<ClientRecord> {
    return this._http.get<ClientRecord>(this.url + '/client/new', httpOptions)
      .pipe(
        catchError(this.handleError<ClientRecord>('getClients'))
      );
  }

  getClientDetail(id: number): Observable<ClientDetail> {
    // httpOptions.params = null;
    return this._http.get<ClientDetail>(this.url + '/client/clientDetails/' + id, httpOptions).pipe(
      catchError(this.handleError<ClientDetail>('get client detail'))
    );
  }

  getCharmList(): Observable<Charm[]> {
    // httpOptions.params = null;
    return this._http.get<Charm[]>(this.url + '/client/charms', httpOptions).pipe(
      catchError(this.handleError<Charm[]>('get client detail'))
    );
  }

  editClient(c: ClientDetail): Promise<any> {
    // httpOptions.params = null;
    let param = {
      detail: JSON.stringify(c)
    };
    return this.http.post('/client/clientDetails', param).toPromise().then(res => {
      console.log(res);
    }).catch(error => {
      console.log(error)
    });

    // return this._http.post(this.url + '/client/clientDetails', param, httpOptions).pipe(
    //   catchError(this.handleError<any>('update client'))
    // );
  }
  deleteClient(id: number): Observable<any> {
    return this._http.delete(this.url + '/client/clientDetails/' + id, httpOptions).pipe(
      catchError(this.handleError<any>('update client'))
    );
  }
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);
      return of(result as T);
    };
  }

}
