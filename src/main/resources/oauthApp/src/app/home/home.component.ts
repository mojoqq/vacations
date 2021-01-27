import {Component, OnInit} from '@angular/core';
import {TokenStorageService} from "../_services/token-storage.service";
import {Observable} from "rxjs";
import {EmployeeDto, EmployeeService, VacationDto} from "../../../generated";


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  start:string = null;
  end:string = null;
  isLoggedIn = false;
  private roles: string[] = [];
  content?: string;
  empl: Observable<EmployeeDto>;
  employee: EmployeeDto;
  displayedColumns: string[] = ['start', 'end', 'edit'];
  userId: number;

  constructor(private tokenStorage: TokenStorageService,
              private employeeApiService: EmployeeService) {
    this.isLoggedIn = !!this.tokenStorage.getToken();
    if (this.isLoggedIn) {
      const user = this.tokenStorage.getUser();
      this.roles = user.roles;
    }
  }

  ngOnInit(): void {
    this.loadVacationsByUsername()
  }

  loadVacationsByUsername() {
    this.userId = this.tokenStorage.getUser().id;
    this.empl = this.employeeApiService.getEmployeeVacations(this.userId);
    this.empl.subscribe(value => {
      this.employee = value;
    })
  }

  onDateChanged(event, vacId: number, startOrEnd: boolean) {
    let date = new Date(event.value).toISOString().slice(0, 10);
    let d = new Date(event.value);
    d.setMinutes(d.getMinutes() - d.getTimezoneOffset());
    d.toISOString().slice(0, 10);
    this.employee.vacations.forEach(value => {
      if (value.id == vacId) {
        if (startOrEnd) {
          // @ts-ignore
          value.start = d;
        } else {
          // @ts-ignore
          value.end = d;
        }
        this.employeeApiService.updateVacationsByEmployeeId(this.employee).subscribe(value1 => {
          this.loadVacationsByUsername()
        });
      }
    })
  }

  addNewVacation() {
   if(this.start != null && this.end != null) {
     let vac:VacationDto = {
       start: this.start,
       end: this.end,
       id: null
     };
     this.employee.vacations.push(vac);
     this.employeeApiService.updateVacationsByEmployeeId(this.employee).subscribe(value => {
       this.loadVacationsByUsername();
       this.start = null;
       this.end = null;
     })
   }
  }

  deleteVacation(id: number) {
    this.employeeApiService.deleteVacationById(id).subscribe(value => this.loadVacationsByUsername())
  }
}
