import {Component, OnInit} from '@angular/core';
import {ClientService} from "../service/client.service";
import {ConfirmationService, LazyLoadEvent} from "primeng/api";
import {FilterParams} from "../../model/FilterParams";
import {ClientRecord} from "../../model/ClientRecord";

@Component({
  selector: 'app-client-list',
  templateUrl: './client-list.component.html',
  styleUrls: ['./client-list.component.css'],
  providers: [ConfirmationService]
})

export class ClientListComponent implements OnInit {
  clients: ClientRecord[];
  client_detail_id: number;
  display: boolean = false;
  selectedClient: ClientRecord;
  header: string;
  EDITEMODE: boolean = false;
  cols: any[];
  nameCols: any[];
  totalRecords: number;
  loading: boolean;
  private filterParams: FilterParams = new FilterParams();
  searchSurname: string;
  searchName: string;
  searchPat: string;

  constructor(private clientService: ClientService, private confirmationService: ConfirmationService) {
  }

  ngOnInit() {
    //columns of table
    this.cols = [
      {field: 'fio', header: 'ФИО'},
      {field: 'charm', header: 'Характер'},
      {field: 'age', header: 'Возраст'},
      {field: 'totalBalance', header: 'Общий остаток счетов'},
      {field: 'maxBalance', header: 'Максимальный остаток'},
      {field: 'minBalance', header: 'Минимальный остаток'}
    ];

    //filter cols
    this.nameCols = ['surname', 'name', 'patronymic'];

    //load client records
    // this.filterParams.limit = 5;
    // this.filterParams.offset = 10;
    // this.getClientRecords(this.filterParams);

    this.loading = false;
  }

  loadLazy(event: LazyLoadEvent) {
    this.filterParams.limit = 5;
    this.getClientRecords(null);
  }

  //output from child listens display value changing
  onChanged(disabled: boolean) {
    this.display = disabled;
  }

  getClientRecords(params): void {
    this.clientService.getClientRecords(params).subscribe((content) => {
      this.clients = content;
      this.totalRecords = this.clients.length;
      console.log(content);
      console.log(this.totalRecords);
    });
  }

  filter(column: string, searchText: string): void {
    this.filterParams.filterCol = column;
    this.filterParams.filter = searchText;
    console.log(this.filterParams.filterCol + ' ' + this.filterParams.filter);
    this.getClientRecords(this.filterParams);
  }

  //on table item selected
  onSelect(c: ClientRecord) {
    this.selectedClient = c;
  }

  edit(id: number) {
    this.header = 'Редактирование клиента';
    this.client_detail_id = id;
    this.EDITEMODE = true;
    this.display = true;
  }

  add() {
    this.header = 'Добавление нового клиента';
    this.EDITEMODE = false;
    this.client_detail_id = 0;
    // this.selectedClient = new ClientRecord();
    this.display = true;
  }

  //confirm for delete
  confirm(id: number) {
    this.confirmationService.confirm({
      message: 'Удалить клиент ' + this.selectedClient.fio + '?',
      header: 'Удаление',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.deleteClient(id);
      },
      reject: () => {
      }
    });
  }

  deleteClient(id: number) {
    this.clients = this.clients.filter(c => c !== this.selectedClient);
    this.clientService.deleteClientDetails(id)
      .subscribe(() =>
        this.clientService.deleteClientRecord(id)
          .subscribe());
  }
}
