window.addEventListener('load', () => {
    const contenedor_loader = document.querySelector('.contenedor')
    contenedor_loader.style.opacity = 0
    contenedor_loader.style.visibility = 'hidden'
})

const app = Vue.createApp({
    data() {
        return {
            cliente: [],
            cuentas: [],
            prestamos: [],
            cuotas: [],
            typeLoan: [],
            amount: null,
            number: "",
            payment: "",
        }
    },
    created() {
        axios.get("/api/clients/current")
            .then(res => {
                this.cliente = res.data
                this.cuentas = res.data.accounts.filter(cuenta => cuenta.active)
                console.log(this.cliente)
            })
            .catch(err => console.log(err))

        axios.get("/api/loans")
            .then(res => {
                this.prestamos = res.data
                console.log(this.prestamos)
                console.log(this.hipetecario)
            })
            .catch(err => console.log(err))
    },
    methods: {
        signOut() {
            axios.post('/api/logout')
                .then(response => {
                    location.href = "/index.html"
                })
        },
        createLoan() {
            swal({
                    title: "Are you sure?",
                    icon: "warning",
                    buttons: true,
                    dangerMode: true,
                })
                .then((willDelete) => {
                    if (willDelete) {
                        axios.post('/api/clients/current/loans', {
                                id: this.typeLoan.id,
                                amount: this.amount,
                                payments: this.payment,
                                number: this.number,
                            })
                            .then(response => {
                                swal({
                                        title: "Congratulations",
                                        text: "amount transferred to selected account",
                                        icon: "success",
                                        button: "OK",
                                    })
                                    .then(res => { location.href = "/accounts.html" })
                            })
                            .catch(err => {
                                if (err.response.data.error == "maximo") {
                                    swal({
                                        title: "Alert",
                                        text: "Amount to request exceeds the maximum",
                                        icon: "warning",
                                    })
                                }
                            })
                    } else {
                        swal("for question contac us");
                    }
                });
        },
    },
    computed: {
        payments() {
            let payList = this.prestamos.filter(prestamo => prestamo.id == this.typeLoan.id)
            let list = [];
            payList.forEach(element => {
                list.push(element.payments)
            })
            return list;
        },
        interest() {
            let payList = this.prestamos.filter(prestamo => prestamo.id == this.typeLoan.id)
            let list = []
            payList.forEach(element => {
                list.push(element.interest)
            })
            return list;

        },
        showMaxAmount() {
            if (this.typeLoan != null) {
                return this.typeLoan.maxAmount;
            }
        }
    },
})
app.mount("#app")

function onlyNumber(e) {
    key = e.keyCode || e.which;
    teclado = String.fromCharCode(key);
    numbers = "0123456789";
    especiales = "8-37-38-46";
    teclado_especial = false;

    for (let i in especiales) {
        if (key == especiales[i]) {
            teclado_especial = true;
        }
    }
    if (numbers.indexOf(teclado) == -1 & !teclado_especial) {
        return false;
    }

}