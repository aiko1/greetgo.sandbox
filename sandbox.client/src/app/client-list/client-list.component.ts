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
  datasource: ClientRecord[];
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
    this.nameCols = [
      {field: 'surname'},
      {field: 'name'},
      {field: 'patronymic'}
    ];

    this.getClientRecords();
    this.loading = true;
  }

  loadLazy(event: LazyLoadEvent) {
    this.loading = true;
    setTimeout(() => {
      if (this.datasource) {
        this.clients = this.datasource.slice(event.first, (event.first + event.rows));
        this.loading = false;
      }
    }, 1000);
  }

  //output from child listens display value changing
  onChanged(disabled: boolean) {
    this.display = disabled;
  }

  getClientRecords(): void {
    this.filterParams.sortBy = 'surname';
    this.filterParams.sortDir = 'ASC';
    this.filterParams.filter = '';
    this.filterParams.filterCol = '';

    this.clientService.getClientRecords(this.filterParams).subscribe((content) => {
      this.datasource = content;
      this.totalRecords = this.datasource.length;
    });
  }

  //on table item selected
  onSelect(c: ClientRecord) {
    this.selectedClient = c;
    this.client_detail_id = c.id;
    console.log(this.client_detail_id);
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
    this.client_detail_id = 1;
    this.selectedClient = new ClientRecord();
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
