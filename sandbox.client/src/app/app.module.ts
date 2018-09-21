import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {LoginComponent} from './login/login.component';
import {HttpService} from "./http.service";
import {HttpClientModule} from "@angular/common/http";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {ClientListComponent} from './client-list/client-list.component';

import {AppRoutingModule} from './app-routing.module';

import {ClientService} from "./service/client.service";
import {DataTableModule, DialogModule, InputTextModule, MessageModule} from "primeng/primeng";
import {TableModule} from "primeng/table";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {ButtonModule} from 'primeng/components/button/button';
import {DropdownModule} from 'primeng/components/dropdown/dropdown';
import {RadioButtonModule} from 'primeng/components/radiobutton/radiobutton';
import {CalendarModule} from 'primeng/components/calendar/calendar';
import {InputMaskModule} from 'primeng/inputmask';
import {KeyFilterModule} from 'primeng/keyfilter';
import {TooltipModule} from 'primeng/tooltip';
import {PaginatorModule} from 'primeng/paginator';
import {ConfirmDialogModule} from 'primeng/confirmdialog';
import { EditClientComponent } from './edit-client/edit-client.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    ClientListComponent,
    EditClientComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    FormsModule,
    AppRoutingModule,
    DataTableModule,
    TableModule,
    DialogModule,
    ButtonModule,
    InputTextModule,
    DropdownModule,
    RadioButtonModule,
    CalendarModule,
    ReactiveFormsModule,
    MessageModule,
    InputMaskModule,
    KeyFilterModule,
    TooltipModule,
    PaginatorModule,
    ConfirmDialogModule
  ],
  providers: [HttpService, ClientService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
