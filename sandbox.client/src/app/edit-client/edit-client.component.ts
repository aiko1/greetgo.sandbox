import {Component, EventEmitter, Input, OnInit, Output, SimpleChanges, ɵConsole} from '@angular/core';
import {ClientDetail} from "../../model/ClientDetail";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {ClientService} from "../service/client.service";
import {Charm} from "../../model/Charm";

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
  charms: Charm[];
  symbols: RegExp = /^[a-zA-Z а-яА-Я]+$/;
  clientform: FormGroup;
  @Output() onChanged = new EventEmitter<boolean>();

  constructor(private _service: ClientService, private fb: FormBuilder) {
    this.charms = [
      {id:1, label: 'спокойный', value: 'спокойный'},
      {id:2, label: 'активный', value: 'активный'},
      {id:3, label: 'аккуратный', value: 'аккуратный'},
      {id:4, label: 'артистичный', value: 'артистичный'},
      {id:5, label: 'бдительный', value: 'бдительный'},
      {id:6, label: 'безобидный', value: 'безобидный'},
      {id:7, label: 'веселый', value: 'веселый'},
      {id:8, label: 'грозный', value: 'грозный'}
    ];
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['header']) {
      console.log(this.header);
    }
  }

  ngOnInit() {
    console.log(this.client_detail_id);

    this.setValidators();

    if (this.client_detail_id) {
      this._service.getClientDetail(this.client_detail_id).subscribe((content) => {
        this.clientDetail = content;
        console.log(content)
      });
    } else {
      this.clientDetail = new ClientDetail();
    }

    console.log(this.header);
  }

  dd: Date;

  //dropdown clicked
  loadCharmList() {
    let d = this.dd.getDate();
    console.log(d + '.' + this.dd.getMonth()+1 + '.' + this.dd.getFullYear());
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
      'homePhoneNumber': new FormControl(''),
      'workPhoneNumber': new FormControl(''),
      'mobileNumber1': new FormControl('', Validators.required),
      'mobileNumber2': new FormControl(''),
      'mobileNumber3': new FormControl('')
    });
  }

  showDate() {
    console.log(this.clientDetail.birthDate);
    console.log(this.client_detail_id);
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
