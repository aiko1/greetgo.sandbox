import {Component, OnInit} from '@angular/core';
import {ClientService} from "../service/client.service";
import {ConfirmationService, LazyLoadEvent} from "primeng/api";
import {FilterParams} from "../../model/FilterParams";
import {ClientRecord} from "../../model/ClientRecord";
import {SortDir} from "../../model/SortDir";
import {SortBy} from "../../model/SortBy";

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
  searchSurname: string;
  searchName: string;
  searchPat: string;

  //sortMode
  fioASC: boolean = false;
  charmASC: boolean = false;
  ageASC: boolean = false;
  totalBalanceASC: boolean = false;
  maxBalanceASC: boolean = false;
  minBalanceASC: boolean = false;
  sortDirection: string = '';
  sortColumn: string = '';

  constructor(private clientService: ClientService, private confirmationService: ConfirmationService) {
  }

  ngOnInit() {
    this.getClientRecords(null);
    console.log(SortBy.FIO);

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

    // this.filterParams.limit = 15;
    this.loading = true;

  }

  loadLazy(event: LazyLoadEvent) {

    this.loading = true;

    setTimeout(() => {
      if (this.datasource) {
        this.clients = this.datasource.slice(event.first, (event.first + event.rows));
        this.loading = false;
      }
    }, 250);
  }

  sort(sortBy: string) {
    console.log(sortBy);

    switch (sortBy) {
      case 'fio': {
        this.sortColumn = SortBy.FIO;
        this.fioASC = !this.fioASC;
        this.sortDirection = (this.fioASC) ? SortDir.ASC : SortDir.DESC;
        break;
      }
      case 'charm': {
        this.sortColumn = SortBy.CHARM;
        this.charmASC = !this.charmASC;
        this.sortDirection = (this.charmASC) ? SortDir.ASC : SortDir.DESC;
        break;
      }
      case 'age': {
        this.sortColumn = SortBy.AGE;
        this.ageASC = !this.ageASC;
        this.sortDirection = (this.ageASC) ? SortDir.ASC : SortDir.DESC;
        break;
      }
      case 'totalBalance': {
        this.sortColumn = SortBy.TOTALBALANCE;
        this.totalBalanceASC = !this.totalBalanceASC;
        this.sortDirection = (this.totalBalanceASC) ? SortDir.ASC : SortDir.DESC;
        break;
      }
      case 'maxBalance': {
        this.sortColumn = SortBy.MAXBALANCE;
        this.maxBalanceASC = !this.maxBalanceASC;
        this.sortDirection = (this.maxBalanceASC) ? SortDir.ASC : SortDir.DESC;
        break;
      }
      case 'minBalance': {
        this.sortColumn = SortBy.MINBALANCE;
        this.minBalanceASC = !this.minBalanceASC;
        this.sortDirection = (this.minBalanceASC) ? SortDir.ASC : SortDir.DESC;
        break;
      }
    }

    this.sorting(this.sortColumn, this.sortDirection);
  }

  sorting(sortBy: string, sortDir: string) {
    this.filterParams = new FilterParams();
    this.filterParams.sortBy = sortBy;
    this.filterParams.sortDir = sortDir;
    console.log('sortBy: ' + this.filterParams.sortBy + ', sortDir: ' + this.filterParams.sortDir)
    this.getClientRecords(this.filterParams);
  }

  //output from child listens display value changing
  onChanged(disabled: boolean) {
    this.display = disabled;
  }

  getClientRecords(params): void {
    this.clientService.getClientRecords(params).subscribe((content) => {
      this.datasource = content;
      this.totalRecords = this.datasource.length;
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

  //confirmForDelete for delete
  confirmForDelete(id: number) {
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
    this.clientService.deleteClient(id).subscribe();
  }
}
