import {Component, Input, OnInit, EventEmitter, Output} from '@angular/core';
import {ClientDetail} from "../../model/ClientDetail";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {SelectItem} from "primeng/api";
import {ClientService} from "../service/client.service";

@Component({
  selector: 'app-edit-client',
  templateUrl: './edit-client.component.html',
  styleUrls: ['./edit-client.component.css']
})
export class EditClientComponent implements OnInit {
  @Input() client_detail_id: number;
  @Input() display: boolean = false;
  @Input() header: string;
  @Input() EDITEMODE: boolean = false;
  clientDetail: ClientDetail;
  birthDate: number = Date.now();
  charms: SelectItem[];
  symbols: RegExp = /^[a-zA-Z а-яА-Я]+$/;
  clientform: FormGroup;
  @Output() onChanged = new EventEmitter<boolean>();

  constructor(private _service: ClientService, private fb: FormBuilder) {
    this.charms = [
      {label: 'спокойный', value: 'спокойный'},
      {label: 'активный', value: 'активный'},
      {label: 'аккуратный', value: 'аккуратный'},
      {label: 'артистичный', value: 'артистичный'},
      {label: 'бдительный', value: 'бдительный'},
      {label: 'безобидный', value: 'безобидный'},
      {label: 'веселый', value: 'веселый'},
      {label: 'грозный', value: 'грозный'}
    ];
  }

  ngOnInit() {
    if (this.client_detail_id) {
      this._service.getClientDetail(this.client_detail_id).subscribe((content) => {
        this.clientDetail = content;
        console.log(content)
      });
    } else {
      this.clientDetail = new ClientDetail();
    }
    this.setValidators();
  }

  setValidators() {
    this.clientform = this.fb.group({
      'surname': new FormControl('', Validators.required),
      'name': new FormControl('', Validators.required),
      'patronymic': new FormControl(''),
      'gender': new FormControl('', Validators.required),
      'bithDate': new FormControl('', Validators.required),
      'charm': new FormControl('', Validators.required),
      'factStreet': new FormControl(''),
      'factNo': new FormControl(''),
      'factFlat': new FormControl(''),
      'regStreet': new FormControl('', Validators.required),
      'regNo': new FormControl('', Validators.required),
      'regFlat': new FormControl('', Validators.required),
      'homePhoneNumber': new FormControl('', Validators.minLength(11)),
      'workPhoneNumber': new FormControl('', Validators.minLength(11)),
      'mobileNumber1': new FormControl('', Validators.compose([Validators.required])),
      'mobileNumber2': new FormControl('', Validators.minLength(11)),
      'mobileNumber3': new FormControl('', Validators.minLength(11))
    });
  }

  showDate() {
    console.log(this.clientDetail.birthDate);
  }

  cancel() {
    if (this.clientform.dirty) {
      alert('Закрыть без сохранения?');
    } else {
      this.close();
    }
  }

  close() {
    this.clientform.reset();
    this.display = false;
    this.onChanged.emit(this.display);
  }


  setClientRecord() {
    // this.selectedClient.surname = this.clientDetail.surname;
    // this.selectedClient.name = this.clientDetail.name;
    // this.selectedClient.patronymic = this.clientDetail.patronymic;
    // this.clientDetail.fio = this.clientDetail.surname + ' ' + this.clientDetail.name + ' ' + this.clientDetail.patronymic;
    // this.selectedClient.fio = this.clientDetail.fio;
    // this.selectedClient.charm = this.clientDetail.charm;
    // this.selectedClient.age = (new Date()).getFullYear() - (+this.clientDetail.birthDate.slice(6));
  }

  saveClient() {
    if (this.EDITEMODE) {
      this.setClientRecord();

      // this._service.updateClientDetail(this.clientDetail)
      //   .subscribe(() =>
      //     this._service.updateClientRecord(this.selectedClient)
      //       .subscribe(() => this.close())
      //   );
    } else {
      this.setClientRecord();

      // this._service.addClientDetailes(this.clientDetail)
      //   .subscribe(() =>
      //     this._service.addClientRecord(this.selectedClient)
      //       .subscribe((c) => {
      //           this.clients.push(c);
      //           this.close();
      //         }
      //       )
      //   );
    }
  }

  onSubmit() {
    if (this.clientform.valid) {
      console.log("Form Submitted!");
      this.clientform.reset();
    }
  }

}
