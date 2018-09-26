import {Component, EventEmitter, Input, OnInit, Output, SimpleChanges, ɵConsole} from '@angular/core';
import {ClientDetail} from "../../model/ClientDetail";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {ClientService} from "../service/client.service";
import {Charm} from "../../model/Charm";


@Component({
  selector: 'app-edit-client',
  templateUrl: './edit-client.component.html',
  styleUrls: ['./edit-client.component.css'],
  providers: []
})
export class EditClientComponent implements OnInit {
  @Input() client_detail_id: number;
  @Input() display: boolean = false;
  @Input() header: string;
  @Input() EDITEMODE: boolean = false;

  clientDetail: ClientDetail;
  charms: Charm[];
  symbols: RegExp = /^[a-zA-Z а-яА-Я]+$/;
  clientform: FormGroup;

  @Output() onChanged = new EventEmitter<boolean>();
  @Output() newClientAdded = new EventEmitter<boolean>();
  @Output() clientEdited = new EventEmitter<boolean>();

  constructor(private _service: ClientService,
              private fb: FormBuilder) {
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['client_detail_id']) {
      console.log(this.client_detail_id);

      if (this.client_detail_id != 0) {
        this._service.getClientDetail(this.client_detail_id).subscribe((content) => {
          this.clientDetail = content;
          console.log(this.clientDetail.birthDate);
        });
      } else {
        this.clientDetail = new ClientDetail();
      }
    }
  }

  ngOnInit() {
    this.setValidators();
    this.loadCharmList();
  }

  //dropdown clicked
  loadCharmList() {
    this._service.getCharmList().subscribe((content) => {
      this.charms = content;
    });
  }

  setValidators() {
    this.clientform = this.fb.group({
      'surname': new FormControl('', Validators.required),
      'name': new FormControl('', Validators.required),
      'patronymic': new FormControl(''),
      'gender': new FormControl('', Validators.required),
      'birthDate': new FormControl('', Validators.required),
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

  saveClient() {
    if (this.EDITEMODE) {
      this._service.editClient(this.clientDetail)
        .then(() => {
          this.clientEdited.emit(true);
          this.close();
        });
    } else {
      this.clientDetail.id = 0;
      this._service.editClient(this.clientDetail)
        .then((c) => {
          this.newClientAdded.emit(true);
          this.close();
        });
    }
  }

  onSubmit() {
    if (this.clientform.valid) {
      console.log("Form Submitted!");
      this.clientform.reset();
    }
  }

}
